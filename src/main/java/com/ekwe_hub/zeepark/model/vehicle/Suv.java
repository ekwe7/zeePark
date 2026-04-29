package com.ekwe_hub.zeepark.model.vehicle;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Document(collection = "vehicles")
public class SUV extends Vehicle{

    public SUV(String numberPlate){
        setNumberPlate(numberPlate);
        setType(VehicleType.SUV);
    }

    @Override
    public BigDecimal calculateBasePrice() {
        return VehicleType.SUV.getBasePrice();
    }
}
