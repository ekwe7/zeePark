package com.ekwe_hub.zeepark.controller;

import com.ekwe_hub.zeepark.dto.request.ParkingStartRequest;
import com.ekwe_hub.zeepark.dto.request.ParkingEndRequest;
import com.ekwe_hub.zeepark.dto.response.ParkingSessionResponse;
import com.ekwe_hub.zeepark.dto.response.ParkingSpotResponse;
import com.ekwe_hub.zeepark.exception.UnauthorizedException;
import com.ekwe_hub.zeepark.filter.SessionFilter;
import com.ekwe_hub.zeepark.mapper.ParkingMapper;
import com.ekwe_hub.zeepark.model.parking.ParkingSession;
import com.ekwe_hub.zeepark.model.user.Customer;
import com.ekwe_hub.zeepark.model.user.User;
import com.ekwe_hub.zeepark.repository.ParkingSpotRepository;
import com.ekwe_hub.zeepark.service.ParkingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking")
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;
    private final ParkingSpotRepository parkingSpotRepository;

    // Customer-facing: view all available spots
    @GetMapping("/spots")
    public List<ParkingSpotResponse> getAvailableSpots() {
        return parkingSpotRepository.findByAvailableTrue().stream()
                .map(s -> new ParkingSpotResponse(s.getId(), s.getZoneId(), s.isAvailable()))
                .toList();
    }

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.CREATED)
    public ParkingSessionResponse startSession(@Valid @RequestBody ParkingStartRequest request,
            HttpServletRequest httpRequest) {
        User currentUser = (User) httpRequest.getAttribute(SessionFilter.AUTHENTICATED_USER_ATTR);
        if (currentUser == null) {
            throw new UnauthorizedException("User not authenticated");
        }
        if (!(currentUser instanceof Customer customer)) {
            throw new UnauthorizedException("Only customers may start parking sessions");
        }
        boolean ownsVehicle = customer.getVehicles().stream()
                .anyMatch(vehicle -> vehicle.getId().equals(request.vehicleId()));
        if (!ownsVehicle) {
            throw new UnauthorizedException("Selected vehicle does not belong to the authenticated user");
        }

        ParkingSession session = parkingService.startSession(currentUser.getId(), request.vehicleId(),
                request.preferredSpotId());
        return ParkingMapper.toDto(session);
    }

    @PostMapping("/end")
    public ParkingSessionResponse endSession(@Valid @RequestBody ParkingEndRequest request) {
        ParkingSession session = parkingService.endSession(request.sessionId());
        return ParkingMapper.toDto(session);
    }

    @GetMapping("/active/{vehicleId}")
    public ParkingSessionResponse getActiveSession(@PathVariable String vehicleId,
            HttpServletRequest httpRequest) {
        User currentUser = (User) httpRequest.getAttribute(SessionFilter.AUTHENTICATED_USER_ATTR);
        if (currentUser == null) {
            throw new UnauthorizedException("User not authenticated");
        }
        ParkingSession session = parkingService.findActiveSessionByUserId(currentUser.getId());
        return ParkingMapper.toDto(session);
    }
}