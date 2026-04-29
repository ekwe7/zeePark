package com.ekwe_hub.zeepark.model.parking;

import com.ekwe_hub.zeepark.model.common.BaseDocument;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Parking_Spot")
public class ParkingSpot extends BaseDocument {
    private String zoneId;
    @DBRef
    private SpotCategory category;
    private boolean available = true;
}
