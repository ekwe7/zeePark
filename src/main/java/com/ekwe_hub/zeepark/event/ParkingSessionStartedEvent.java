package com.ekwe_hub.zeepark.event;

import java.time.LocalDateTime;

public record ParkingSessionStartedEvent(
        String sessionId,
        String vehicleId,
        LocalDateTime entryTime) {}
