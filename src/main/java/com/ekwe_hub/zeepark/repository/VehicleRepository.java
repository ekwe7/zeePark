package com.ekwe_hub.zeepark.repository;

import com.ekwe_hub.zeepark.model.vehicle.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VehicleRepository extends MongoRepository<Vehicle, String> {
    Optional<Vehicle> findByNumberPlate(String numberPlate);
}
