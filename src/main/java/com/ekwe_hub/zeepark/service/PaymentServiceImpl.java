package com.ekwe_hub.zeepark.service;

import com.ekwe_hub.zeepark.event.PaymentCompletedEvent;
import com.ekwe_hub.zeepark.exception.PaymentFailedException;
import com.ekwe_hub.zeepark.exception.ResourceNotFoundException;
import com.ekwe_hub.zeepark.model.parking.ParkingSession;
import com.ekwe_hub.zeepark.model.parking.SessionStatus;
import com.ekwe_hub.zeepark.model.payment.Payment;
import com.ekwe_hub.zeepark.model.payment.PaymentMethod;
import com.ekwe_hub.zeepark.model.payment.PaymentRequest;
import com.ekwe_hub.zeepark.model.payment.TransactionResult;
import com.ekwe_hub.zeepark.model.vehicle.Vehicle;
import com.ekwe_hub.zeepark.repository.ParkingSessionRepository;
import com.ekwe_hub.zeepark.repository.PaymentRepository;
import com.ekwe_hub.zeepark.repository.VehicleRepository;
import com.ekwe_hub.zeepark.service.payment.PaymentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final Map<String, PaymentGateway> gateways;
    private final PaymentRepository paymentRepository;
    private final ParkingSessionRepository sessionRepository;
    private final VehicleRepository vehicleRepository;
    private final ApplicationEventPublisher eventPublisher;

    private static final String CURRENCY = "USD";

    @Override
    @Transactional
    public Payment processPayment(String sessionId, PaymentMethod method) {
        ParkingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found: " + sessionId));

        if (session.getStatus() != SessionStatus.COMPLETED) {
            throw new IllegalStateException("Session must be completed before payment");
        }

        Vehicle vehicle = vehicleRepository.findById(session.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found for session: " + sessionId));

        // Rate: basePrice per hour, prorated by minutes
        BigDecimal basePrice = vehicle.calculateBasePrice();
        BigDecimal amount = basePrice
                .multiply(BigDecimal.valueOf(session.getDuration()))
                .divide(BigDecimal.valueOf(60), 2, java.math.RoundingMode.HALF_UP);

        PaymentGateway gateway = gateways.get(method.name().toLowerCase());
        if (gateway == null) {
            throw new IllegalArgumentException("Unsupported payment method: " + method);
        }

        TransactionResult result = gateway.charge(
                new PaymentRequest(amount, CURRENCY, "Parking session " + sessionId)
        );

        if (!result.success()) {
            throw new PaymentFailedException(result.message());
        }

        Payment payment = new Payment();
        payment.setSessionId(sessionId);
        payment.setAmount(amount);
        payment.setCurrency(CURRENCY);
        payment.setMethod(method);
        payment.setPaidAt(LocalDateTime.now());
        payment.setTransactionId(result.transactionId());
        Payment saved = paymentRepository.save(payment);

        eventPublisher.publishEvent(new PaymentCompletedEvent(
                saved.getId(), sessionId, amount
        ));

        return saved;
    }

    @Override
    public Payment findBySessionId(String sessionId) {
        return paymentRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("No payment found for session: " + sessionId));
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }
}