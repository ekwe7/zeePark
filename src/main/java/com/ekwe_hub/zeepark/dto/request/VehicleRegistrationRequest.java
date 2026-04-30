package com.ekwe_hub.zeepark.dto.request;

import com.ekwe_hub.zeepark.model.vehicle.VehicleType;

public record VehicleRegistrationRequest(String numberPlate, VehicleType type) {
}
