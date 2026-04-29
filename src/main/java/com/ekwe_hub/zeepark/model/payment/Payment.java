package com.ekwe_hub.zeepark.model.payment;

import com.ekwe_hub.zeepark.model.common.BaseDocument;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "payments")
public class Payment extends BaseDocument {
    private String sessionId;
    private BigDecimal amount;
    private String currency;
    private PaymentMethod method;
    private LocalDateTime paidAt;
    private String transactionId;
}
