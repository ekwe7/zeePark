package com.ekwe_hub.zeepark.service.payment;

import com.ekwe_hub.zeepark.model.payment.PaymentRequest;
import com.ekwe_hub.zeepark.model.payment.TransactionResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("binance")
public class BinanceGateway implements PaymentGateway {

    private final String apiKey;
    private final String secretKey;

    public BinanceGateway(@Value("${payment.binance.api-key}") String apiKey,
                          @Value("${payment.binance.secret-key}") String secretKey) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }

    @Override
    public TransactionResult charge(PaymentRequest request) {
        // Simulate Binance Pay API call
        return new TransactionResult(
                "bn_txn_" + System.currentTimeMillis(),
                true,
                "Binance payment successful"
        );
    }
}