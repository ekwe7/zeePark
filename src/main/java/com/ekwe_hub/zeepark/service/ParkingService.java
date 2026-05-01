package com.ekwe_hub.zeepark.service;

import com.ekwe_hub.zeepark.model.parking.ParkingSession;

public interface ParkingService {
    ParkingSession startSession(String vehicleId, String preferredSpotId);
    ParkingSession endSession(String sessionId);
    ParkingSession findActiveSessionByVehicleId(String vehicleId);
}