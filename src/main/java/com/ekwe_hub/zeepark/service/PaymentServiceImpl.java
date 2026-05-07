package com.ekwe_hub.zeepark.service;

import com.ekwe_hub.zeepark.dto.response.CheckoutResponse;
import com.ekwe_hub.zeepark.event.PaymentCompletedEvent;
import com.ekwe_hub.zeepark.exception.PaymentFailedException;
import com.ekwe_hub.zeepark.exception.ResourceNotFoundException;
import com.ekwe_hub.zeepark.model.parking.ParkingSession;
import com.ekwe_hub.zeepark.model.parking.SessionStatus;
import com.ekwe_hub.zeepark.model.payment.*;
import com.ekwe_hub.zeepark.model.vehicle.Vehicle;
import com.ekwe_hub.zeepark.model.user.Customer;
import com.ekwe_hub.zeepark.repository.ParkingSessionRepository;
import com.ekwe_hub.zeepark.repository.PaymentRepository;
import com.ekwe_hub.zeepark.repository.UserRepository;
import com.ekwe_hub.zeepark.repository.VehicleRepository;
import com.ekwe_hub.zeepark.service.payment.PaymentGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final Map<String, PaymentGateway> gateways;
    private final PaymentRepository paymentRepository;
    private final ParkingSessionRepository sessionRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${payment.flutterwave.secret-key:}")
    private String flwSecretKey;

    private static final String CURRENCY = "NGN";

    @Override
    @Transactional
    public CheckoutResponse initiatePayment(String sessionId, PaymentMethod method, String email) {
        ParkingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found: " + sessionId));

        if (session.getStatus() != SessionStatus.COMPLETED) {
            throw new IllegalStateException("Session must be completed before payment");
        }

        Vehicle vehicle = vehicleRepository.findById(session.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found for session: " + sessionId));

        BigDecimal basePrice = vehicle.calculateBasePrice();
        BigDecimal amount = basePrice
                .multiply(BigDecimal.valueOf(session.getDuration()))
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        String gatewayKey = method.name().toLowerCase();
        PaymentGateway gateway = gateways.get(gatewayKey);
        if (gateway == null) {
            throw new IllegalArgumentException("Unsupported payment method: " + method);
        }

        TransactionResult result = gateway.charge(
                new PaymentRequest(amount, CURRENCY, "ZeePark session " + sessionId, email));

        if (!result.success()) {
            throw new PaymentFailedException("Payment initiation failed: " + result.message());
        }

        // Save payment as PENDING with checkout URL
        Payment payment = new Payment();
        payment.setSessionId(sessionId);
        payment.setAmount(amount);
        payment.setCurrency(CURRENCY);
        payment.setMethod(method);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCheckoutUrl(result.message()); // message holds the checkout URL
        payment.setTransactionId(result.transactionId()); // tx_ref
        Payment saved = paymentRepository.save(payment);

        return new CheckoutResponse(saved.getId(), result.message());
    }

    @Override
    @Transactional
    public Payment verifyPayment(String transactionId, String txRef) {
        // Verify with Flutterwave
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(flwSecretKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://api.flutterwave.com/v3/transactions/" + transactionId + "/verify",
                    HttpMethod.GET, entity, Map.class);

            Map<String, Object> body = response.getBody();
            if (body != null && "success".equals(body.get("status"))) {
                Map<String, Object> data = (Map<String, Object>) body.get("data");
                String status = (String) data.get("status");

                Payment payment = paymentRepository.findByTransactionId(txRef)
                        .orElseThrow(() -> new ResourceNotFoundException("Payment not found for tx_ref: " + txRef));

                if ("successful".equals(status)) {
                    payment.setStatus(PaymentStatus.COMPLETED);
                    payment.setPaidAt(LocalDateTime.now());
                    payment.setProviderTransactionId(transactionId);
                    paymentRepository.save(payment);

                    eventPublisher.publishEvent(new PaymentCompletedEvent(
                            payment.getId(), payment.getSessionId(), payment.getAmount()));
                } else {
                    payment.setStatus(PaymentStatus.FAILED);
                    paymentRepository.save(payment);
                }
                return payment;
            }
        } catch (Exception e) {
            log.error("Flutterwave verification error: {}", e.getMessage());
        }
        throw new PaymentFailedException("Could not verify payment");
    }

    @Override
    public Payment findBySessionId(String sessionId) {
        return paymentRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("No payment found for session: " + sessionId));
    }

    @Override
    public List<Payment> findByCustomerId(String customerId) {
        // Get customer's vehicle IDs
        return userRepository.findById(customerId)
                .filter(u -> u instanceof Customer)
                .map(u -> {
                    List<String> vehicleIds = ((Customer) u).getVehicles().stream()
                            .map(v -> v.getId())
                            .toList();
                    // Get sessions for those vehicles
                    List<String> sessionIds = sessionRepository.findByVehicleIdIn(vehicleIds).stream()
                            .map(s -> s.getId())
                            .toList();
                    // Get payments for those sessions
                    return paymentRepository.findBySessionIdIn(sessionIds);
                })
                .orElse(List.of());
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }
}
