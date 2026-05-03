package com.ekwe_hub.zeepark.controller;

import com.ekwe_hub.zeepark.dto.request.VehicleRegistrationRequest;
import com.ekwe_hub.zeepark.dto.response.VehicleResponse;
import com.ekwe_hub.zeepark.mapper.VehicleMapper;
import com.ekwe_hub.zeepark.model.vehicle.Vehicle;
import com.ekwe_hub.zeepark.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponse registerVehicle(@Valid @RequestBody VehicleRegistrationRequest request) {
        Vehicle vehicle = vehicleService.registerVehicle(request);
        return VehicleMapper.toDto(vehicle);
    }

    @GetMapping("/{numberPlate}")
    public VehicleResponse getVehicle(@PathVariable String numberPlate) {
        Vehicle vehicle = vehicleService.findByNumberPlate(numberPlate);
        return VehicleMapper.toDto(vehicle);
    }

    @GetMapping
    public List<VehicleResponse> getAllVehicles() {
        return vehicleService.findAll().stream()
                .map(VehicleMapper::toDto)
                .toList();
    }

    // Returns only vehicles belonging to a specific customer
    @GetMapping("/my/{customerId}")
    public List<VehicleResponse> getMyVehicles(@PathVariable String customerId) {
        return vehicleService.findByCustomerId(customerId).stream()
                .map(VehicleMapper::toDto)
                .toList();
    }

    @DeleteMapping("/{numberPlate}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVehicle(@PathVariable String numberPlate) {
        vehicleService.deleteByNumberPlate(numberPlate);
    }
}