package com.ekwe_hub.zeepark.service.payment;

import com.ekwe_hub.zeepark.model.payment.PaymentRequest;
import com.ekwe_hub.zeepark.model.payment.TransactionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("paypal")
@Slf4j
public class PayPalGateway implements PaymentGateway {

    @Override
    public TransactionResult charge(PaymentRequest request) {
        // PayPal integration coming soon
        log.info("PayPal payment initiated for amount: {}", request.amount());
        return new TransactionResult(null, false, "PayPal integration not yet configured");
    }
}
