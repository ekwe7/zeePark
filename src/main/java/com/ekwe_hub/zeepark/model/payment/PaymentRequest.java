package com.ekwe_hub.zeepark.model.payment;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        String currency,
        String description) {
}
