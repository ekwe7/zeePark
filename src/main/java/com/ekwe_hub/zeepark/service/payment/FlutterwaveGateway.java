package com.ekwe_hub.zeepark.service.payment;

import com.ekwe_hub.zeepark.model.payment.PaymentRequest;
import com.ekwe_hub.zeepark.model.payment.TransactionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component("flutterwave")
@Slf4j
public class FlutterwaveGateway implements PaymentGateway {

    private static final String FLW_INIT_URL = "https://api.flutterwave.com/v3/payments";

    private final String secretKey;
    private final String publicKey;
    private final String redirectUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public FlutterwaveGateway(
            @Value("${payment.flutterwave.secret-key}") String secretKey,
            @Value("${payment.flutterwave.public-key}") String publicKey,
            @Value("${payment.flutterwave.redirect-url}") String redirectUrl) {
        this.secretKey = secretKey;
        this.publicKey = publicKey;
        this.redirectUrl = redirectUrl;
    }

    @Override
    public TransactionResult charge(PaymentRequest request) {
        // Build Flutterwave payment initialization payload
        Map<String, Object> body = new HashMap<>();
        body.put("tx_ref", "ZP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        body.put("amount", request.amount().toString());
        body.put("currency", request.currency() != null ? request.currency() : "NGN");
        body.put("redirect_url", redirectUrl);
        body.put("payment_options", "card,banktransfer,ussd");

        // Customer info
        Map<String, String> customer = new HashMap<>();
        customer.put("email", request.email() != null ? request.email() : "customer@zeepark.com");
        customer.put("name", "ZeePark Customer");
        body.put("customer", customer);

        // Customization
        Map<String, String> customization = new HashMap<>();
        customization.put("title", "ZeePark Payment");
        customization.put("description", request.description());
        customization.put("logo", "");
        body.put("customizations", customization);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(secretKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(FLW_INIT_URL, entity, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null && "success".equals(responseBody.get("status"))) {
                Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
                String checkoutUrl = (String) data.get("link");
                String txRef = (String) body.get("tx_ref");
                log.info("Flutterwave checkout created: {}", txRef);
                return new TransactionResult(txRef, true, checkoutUrl);
            } else {
                String message = responseBody != null ? (String) responseBody.get("message") : "Unknown error";
                log.error("Flutterwave init failed: {}", message);
                return new TransactionResult(null, false, message);
            }
        } catch (Exception e) {
            log.error("Flutterwave request error: {}", e.getMessage());
            return new TransactionResult(null, false, e.getMessage());
        }
    }
}
