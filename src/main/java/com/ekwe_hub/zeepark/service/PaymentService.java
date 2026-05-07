package com.ekwe_hub.zeepark.service;

import com.ekwe_hub.zeepark.dto.response.CheckoutResponse;
import com.ekwe_hub.zeepark.model.payment.Payment;
import com.ekwe_hub.zeepark.model.payment.PaymentMethod;

import java.util.List;

public interface PaymentService {
    // Returns checkout URL for redirect-based payments (Flutterwave, PayPal)
    CheckoutResponse initiatePayment(String sessionId, PaymentMethod method, String email);

    // Verify payment after redirect
    Payment verifyPayment(String transactionId, String txRef);

    Payment findBySessionId(String sessionId);

    List<Payment> findByCustomerId(String customerId);

    List<Payment> findAll();
}
