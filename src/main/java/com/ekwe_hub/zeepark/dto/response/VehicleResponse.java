package com.ekwe_hub.zeepark.dto.response;

import com.ekwe_hub.zeepark.model.vehicle.VehicleType;

import java.math.BigDecimal;

public record VehicleResponse(
        String id,
        String numberPlate,
        VehicleType type,
        BigDecimal baseRate
) {}
