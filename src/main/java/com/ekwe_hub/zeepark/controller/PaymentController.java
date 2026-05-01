package com.ekwe_hub.zeepark.controller;

import com.ekwe_hub.zeepark.dto.request.CheckoutRequest;
import com.ekwe_hub.zeepark.dto.request.PaymentRequestDto;
import com.ekwe_hub.zeepark.dto.response.CheckoutResponse;
import com.ekwe_hub.zeepark.dto.response.PaymentResponse;
import com.ekwe_hub.zeepark.mapper.PaymentMapper;
import com.ekwe_hub.zeepark.model.payment.Payment;
import com.ekwe_hub.zeepark.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // ---------- Direct Payment (charge immediately) ----------
    @PostMapping
    public PaymentResponse processPayment(@Valid @RequestBody PaymentRequestDto request) {
        Payment payment = paymentService.processPayment(request.sessionId(), request.method());
        return PaymentMapper.toDto(payment);
    }

    // ---------- Checkout Flow ----------
    @PostMapping("/checkout")
    public CheckoutResponse createCheckout(@RequestBody CheckoutRequest request) {
        return paymentService.initiateCheckout(
                request.sessionId(),
                request.method(),
                request.successUrl(),
                request.cancelUrl(),
                request.email()
        );
    }

    // ---------- Webhook Callback ----------
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader(value = "Stripe-Signature", required = false) String sigHeader) {
        boolean ok = paymentService.processWebhook(payload, sigHeader);
        return ok ? ResponseEntity.ok("OK") : ResponseEntity.badRequest().body("Invalid");
    }

    // ---------- Query endpoints ----------
    @GetMapping("/session/{sessionId}")
    public PaymentResponse getPaymentBySession(@PathVariable String sessionId) {
        Payment payment = paymentService.findBySessionId(sessionId);
        return PaymentMapper.toDto(payment);
    }

    @GetMapping
    public List<PaymentResponse> getAllPayments() {
        return paymentService.findAll().stream()
                .map(PaymentMapper::toDto)
                .toList();
    }
}