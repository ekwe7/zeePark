package com.ekwe_hub.zeepark.service;

import com.ekwe_hub.zeepark.dto.request.VehicleRegistrationRequest;
import com.ekwe_hub.zeepark.exception.ResourceNotFoundException;
import com.ekwe_hub.zeepark.model.vehicle.*;
import com.ekwe_hub.zeepark.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Override
    public Vehicle registerVehicle(VehicleRegistrationRequest request) {
        Vehicle vehicle = switch (request.type()) {
            case BICYCLE -> new Bike(request.numberPlate());
            case SUV -> new Suv(request.numberPlate());
            case EV -> new Ev(request.numberPlate());
            case CAR -> new Car(request.numberPlate(), request.type());
            default -> throw new IllegalArgumentException("Unsupported vehicle type: " + request.type());
        };
        return vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle findByNumberPlate(String numberPlate) {
        return vehicleRepository.findByNumberPlate(numberPlate)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + numberPlate));
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @Override
    public void deleteByNumberPlate(String numberPlate) {
        Vehicle vehicle = findByNumberPlate(numberPlate);
        vehicleRepository.delete(vehicle);
    }
}