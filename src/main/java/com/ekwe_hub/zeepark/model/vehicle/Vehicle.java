package com.ekwe_hub.zeepark.model.vehicle;

import com.ekwe_hub.zeepark.model.common.BaseDocument;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "vehicles")
public abstract class Vehicle extends BaseDocument {
    private String numberPlate;
    private VehicleType type;

    public abstract BigDecimal calculateBasePrice();
}
