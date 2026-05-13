package com.ekwe_hub.zeepark.mapper;

import com.ekwe_hub.zeepark.dto.response.ParkingSessionResponse;
import com.ekwe_hub.zeepark.model.parking.ParkingSession;

public class ParkingMapper {
    public static ParkingSessionResponse toDto(ParkingSession session) {
        return new ParkingSessionResponse(
                session.getUserId(),
                session.getVehicleId(),
                session.getSpotId(),
                session.getEntryTime(),
                session.getExitTime(),
                session.getStatus(),
                session.getDuration());
    }
}