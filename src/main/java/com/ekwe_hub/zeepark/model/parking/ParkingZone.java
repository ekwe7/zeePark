package com.ekwe_hub.zeepark.model.parking;

import com.ekwe_hub.zeepark.model.common.Address;
import com.ekwe_hub.zeepark.model.common.BaseDocument;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document("parking-zone")
public class ParkingZone extends BaseDocument {
    private String name;
    private Zonelevel level;
    private Address address;
}
