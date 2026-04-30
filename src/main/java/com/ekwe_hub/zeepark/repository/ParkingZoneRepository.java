package com.ekwe_hub.zeepark.repository;

import com.ekwe_hub.zeepark.model.parking.ParkingZone;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ParkingZoneRepository extends MongoRepository<ParkingZone, String> {
}
