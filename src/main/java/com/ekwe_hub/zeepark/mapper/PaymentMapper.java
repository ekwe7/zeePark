package com.ekwe_hub.zeepark.mapper;

import com.ekwe_hub.zeepark.dto.response.PaymentResponse;
import com.ekwe_hub.zeepark.model.payment.Payment;

public class PaymentMapper {
    public static PaymentResponse toDto(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getSessionId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getCheckoutUrl(),
                payment.getPaidAt(),
                payment.getTransactionId()
        );
    }
}
