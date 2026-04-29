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
@Document(collection = "Parking_Session")
public class ParkingSession extends BaseDocument {
    private String vehicleId;
    private String spotId;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private SessionStatus status;
    private int duration;
}
