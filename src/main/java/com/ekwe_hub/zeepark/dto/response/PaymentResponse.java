package com.ekwe_hub.zeepark.dto.response;

import com.ekwe_hub.zeepark.model.payment.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        String id,
        String sessionId,
        BigDecimal amount,
        PaymentMethod method,
        LocalDateTime paidAt,
        String transactionId
) {}
