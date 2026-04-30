package com.ekwe_hub.zeepark.service.payment;

import com.ekwe_hub.zeepark.model.payment.PaymentRequest;
import com.ekwe_hub.zeepark.model.payment.TransactionResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("mastercard")
public class MastercardGateway implements PaymentGateway {

    private final String apiKey;
    private final String merchantId;

    public MastercardGateway(@Value("${payment.mastercard.api-key}") String apiKey,
                             @Value("${payment.mastercard.merchant-id}") String merchantId) {
        this.apiKey = apiKey;
        this.merchantId = merchantId;
    }

    @Override
    public TransactionResult charge(PaymentRequest request) {
        // Simulate Mastercard API call
        return new TransactionResult(
                "mc_txn_" + System.currentTimeMillis(),
                true,
                "Mastercard payment successful"
        );
    }
}