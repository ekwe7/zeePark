package com.ekwe_hub.zeepark.model.payment;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        String currency,
        String description,
        String email) {

    // Backward-compatible constructor without email
    public PaymentRequest(BigDecimal amount, String currency, String description) {
        this(amount, currency, description, null);
    }
}
