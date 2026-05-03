package com.ekwe_hub.zeepark.event;

import java.math.BigDecimal;

public record PaymentCompletedEvent(
        String paymentId,
        String sessionId,
        BigDecimal amount) {}
