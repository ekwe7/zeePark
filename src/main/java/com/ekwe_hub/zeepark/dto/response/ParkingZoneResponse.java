package com.ekwe_hub.zeepark.dto.response;

import com.ekwe_hub.zeepark.model.parking.Zonelevel;

public record ParkingZoneResponse(
        String id,
        String name,
        Zonelevel level
) {}
