package com.ekwe_hub.zeepark.dto.response;

import com.ekwe_hub.zeepark.model.parking.SessionStatus;

import java.time.LocalDateTime;

public record ParkingSessionResponse(
        String id,
        String vehicleId,
        String spotId,
        LocalDateTime entryTime,
        LocalDateTime exitTime,
        SessionStatus status,
        int duration) {}
