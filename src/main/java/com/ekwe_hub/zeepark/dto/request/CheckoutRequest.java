package com.ekwe_hub.zeepark.dto.request;

import com.ekwe_hub.zeepark.model.payment.PaymentMethod;

public record CheckoutRequest(
        String sessionId,
        PaymentMethod method,
        String successUrl,
        String cancelUrl,
        String email
) {}
