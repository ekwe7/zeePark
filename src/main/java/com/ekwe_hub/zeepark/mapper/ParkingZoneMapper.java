package com.ekwe_hub.zeepark.mapper;

import com.ekwe_hub.zeepark.dto.response.ParkingZoneResponse;
import com.ekwe_hub.zeepark.model.parking.ParkingZone;

public class ParkingZoneMapper {
    public static ParkingZoneResponse toDto(ParkingZone zone) {
        return new ParkingZoneResponse(
                zone.getId(),
                zone.getName(),
                zone.getLevel()
        );
    }
}
