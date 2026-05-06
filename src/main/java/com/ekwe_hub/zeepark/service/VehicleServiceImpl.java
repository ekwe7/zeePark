package com.ekwe_hub.zeepark.service;

import com.ekwe_hub.zeepark.dto.request.VehicleRegistrationRequest;
import com.ekwe_hub.zeepark.exception.ResourceNotFoundException;
import com.ekwe_hub.zeepark.model.user.Customer;
import com.ekwe_hub.zeepark.model.vehicle.*;
import com.ekwe_hub.zeepark.repository.UserRepository;
import com.ekwe_hub.zeepark.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;          // <-- NEW

    @Override
    @Transactional                                       // <-- ensures both saves succeed or rollback
    public Vehicle registerVehicle(VehicleRegistrationRequest request) {
        //Create the correct concrete Vehicle
        Vehicle vehicle = switch (request.type()) {
            case BICYCLE -> new Bike(request.numberPlate());
            case SUV  -> new Suv(request.numberPlate());
            case EV   -> new Ev(request.numberPlate());
            case CAR  -> new Car(request.numberPlate(), request.type());
            default   -> throw new IllegalArgumentException("Unsupported vehicle type: " + request.type());
        };
        final Vehicle savedVehicle = vehicleRepository.save(vehicle);

        // Link to the customer safely
        userRepository.findById(request.customerId())
                .ifPresent(user -> {
                    if (user instanceof Customer customer) {
                        customer.getVehicles().add(savedVehicle);
                        userRepository.save(customer);
                    }
                });

        return savedVehicle;
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
    public List<Vehicle> findByCustomerId(String customerId) {
        return userRepository.findById(customerId)
                .filter(u -> u instanceof Customer)
                .map(u -> ((Customer) u).getVehicles())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerId));
    }

    @Override
    @Transactional
    public void deleteByNumberPlate(String numberPlate) {
        Vehicle vehicle = findByNumberPlate(numberPlate);
        // Remove from customer's vehicle list
        userRepository.findAll().forEach(user -> {
            if (user instanceof Customer customer) {
                boolean removed = customer.getVehicles().removeIf(v -> v.getId().equals(vehicle.getId()));
                if (removed) userRepository.save(customer);
            }
        });
        vehicleRepository.delete(vehicle);
    }
}