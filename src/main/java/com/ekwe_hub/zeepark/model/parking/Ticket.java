package com.ekwe_hub.zeepark.model.parking;

import com.ekwe_hub.zeepark.model.common.BaseDocument;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "tickets")
public class Ticket extends BaseDocument {
    private String sessionId;
    private String vehicleNumberPlate;
    private LocalDateTime issuedAt;
}
