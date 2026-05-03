package com.ekwe_hub.zeepark.dto.request;

import com.ekwe_hub.zeepark.model.payment.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentRequestDto(
        @NotBlank String sessionId,
        @NotNull PaymentMethod method,
        String email
) {}
