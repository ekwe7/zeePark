package com.ekwe_hub.zeepark.service;

import com.ekwe_hub.zeepark.model.payment.Payment;
import com.ekwe_hub.zeepark.model.payment.PaymentMethod;

import java.util.List;

public interface PaymentService {
    Payment processPayment(String sessionId, PaymentMethod method);
    Payment findBySessionId(String sessionId);
    List<Payment> findAll();
}
