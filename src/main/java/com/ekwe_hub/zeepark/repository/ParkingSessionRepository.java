package com.ekwe_hub.zeepark.repository;

import com.ekwe_hub.zeepark.model.parking.ParkingSession;
import com.ekwe_hub.zeepark.model.parking.SessionStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ParkingSessionRepository extends MongoRepository<ParkingSession, String> {
    List<ParkingSession> findByVehicleIdAndStatus(String vehicleId, SessionStatus status);
    Optional<ParkingSession> findByIdAndStatus(String id, SessionStatus status);
}
