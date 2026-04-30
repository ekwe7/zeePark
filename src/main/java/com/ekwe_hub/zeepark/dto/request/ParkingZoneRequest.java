package com.ekwe_hub.zeepark.dto.request;

import com.ekwe_hub.zeepark.model.parking.Zonelevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ParkingZoneRequest(
        @NotBlank String name,
        @NotNull Zonelevel level
) {}
