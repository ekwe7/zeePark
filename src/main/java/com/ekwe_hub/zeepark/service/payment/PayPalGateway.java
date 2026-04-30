package com.ekwe_hub.zeepark.service.payment;

import com.ekwe_hub.zeepark.model.payment.PaymentRequest;
import com.ekwe_hub.zeepark.model.payment.TransactionResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("paypal")
public class PayPalGateway implements PaymentGateway {

    private final String clientId;
    private final String secret;

    public PayPalGateway(@Value("${payment.paypal.client-id}") String clientId,
                         @Value("${payment.paypal.secret}") String secret) {
        this.clientId = clientId;
        this.secret = secret;
    }

    @Override
    public TransactionResult charge(PaymentRequest request) {
        // Simulate PayPal API call
        return new TransactionResult(
                "pp_txn_" + System.currentTimeMillis(),
                true,
                "PayPal payment successful"
        );
    }
}