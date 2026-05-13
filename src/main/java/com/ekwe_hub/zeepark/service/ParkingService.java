package com.ekwe_hub.zeepark.service;

import com.ekwe_hub.zeepark.model.parking.ParkingSession;

public interface ParkingService {
    ParkingSession startSession(String userId, String vehicleId, String preferredSpotId);

    ParkingSession endSession(String sessionId);

    ParkingSession findActiveSessionByUserId(String userId);

    ParkingSession findActiveSessionByVehicleId(String vehicleId);
}