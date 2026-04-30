package com.ekwe_hub.zeepark.mapper;

import com.ekwe_hub.zeepark.dto.response.VehicleResponse;
import com.ekwe_hub.zeepark.model.vehicle.Vehicle;

public class VehicleMapper {
    public static VehicleResponse toDto(Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getNumberPlate(),
                vehicle.getType(),
                vehicle.calculateBasePrice()
        );
    }
}
