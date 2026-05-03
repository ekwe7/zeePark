package com.ekwe_hub.zeepark.controller;

import com.ekwe_hub.zeepark.dto.request.ParkingStartRequest;
import com.ekwe_hub.zeepark.dto.request.ParkingEndRequest;
import com.ekwe_hub.zeepark.dto.response.ParkingSessionResponse;
import com.ekwe_hub.zeepark.mapper.ParkingMapper;
import com.ekwe_hub.zeepark.model.parking.ParkingSession;
import com.ekwe_hub.zeepark.service.ParkingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parking")
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.CREATED)
    public ParkingSessionResponse startSession(@Valid @RequestBody ParkingStartRequest request) {
        ParkingSession session = parkingService.startSession(request.vehicleId(), request.preferredSpotId());
        return ParkingMapper.toDto(session);
    }

    @PostMapping("/end")
    public ParkingSessionResponse endSession(@Valid @RequestBody ParkingEndRequest request) {
        ParkingSession session = parkingService.endSession(request.sessionId());
        return ParkingMapper.toDto(session);
    }

    @GetMapping("/active/{vehicleId}")
    public ParkingSessionResponse getActiveSession(@PathVariable String vehicleId) {
        ParkingSession session = parkingService.findActiveSessionByVehicleId(vehicleId);
        return ParkingMapper.toDto(session);
    }
}