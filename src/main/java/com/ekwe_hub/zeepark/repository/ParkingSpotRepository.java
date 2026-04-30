package com.ekwe_hub.zeepark.repository;

import com.ekwe_hub.zeepark.model.parking.ParkingSpot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ParkingSpotRepository extends MongoRepository<ParkingSpot, String> {
    List<ParkingSpot> findByAvailableTrue();
}
