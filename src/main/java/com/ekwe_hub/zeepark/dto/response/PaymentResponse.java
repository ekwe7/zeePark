package com.ekwe_hub.zeepark.dto.response;

import com.ekwe_hub.zeepark.model.payment.PaymentMethod;
import com.ekwe_hub.zeepark.model.payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        String id,
        String sessionId,
        BigDecimal amount,
        String currency,
        PaymentMethod method,
        PaymentStatus status,
        String checkoutUrl,
        LocalDateTime paidAt,
        String transactionId
) {}
