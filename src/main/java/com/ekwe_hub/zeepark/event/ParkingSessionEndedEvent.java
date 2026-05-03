package com.ekwe_hub.zeepark.event;

import java.time.LocalDateTime;

public record ParkingSessionEndedEvent(
        String sessionId,
        String spotId,
        LocalDateTime exitTime,
        int duration
) {}
