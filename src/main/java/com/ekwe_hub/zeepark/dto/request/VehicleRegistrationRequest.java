package com.ekwe_hub.zeepark.dto.request;

import com.ekwe_hub.zeepark.model.vehicle.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VehicleRegistrationRequest(
        @NotBlank String numberPlate,
        @NotNull VehicleType type,
        @NotBlank String customerId
) {
}
