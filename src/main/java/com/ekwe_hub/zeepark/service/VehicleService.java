package com.ekwe_hub.zeepark.service;

import com.ekwe_hub.zeepark.dto.request.VehicleRegistrationRequest;
import com.ekwe_hub.zeepark.model.vehicle.Vehicle;

import java.util.List;

public interface VehicleService {
    Vehicle registerVehicle(VehicleRegistrationRequest request);
    Vehicle findByNumberPlate(String numberPlate);
    List<Vehicle> findAll();
    List<Vehicle> findByCustomerId(String customerId);
    void deleteByNumberPlate(String numberPlate);
}