package com.ekwe_hub.zeepark.service.payment;

import com.ekwe_hub.zeepark.model.payment.PaymentRequest;
import com.ekwe_hub.zeepark.model.payment.TransactionResult;

public interface PaymentGateway {
    TransactionResult charge(PaymentRequest request);
}
