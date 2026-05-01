package com.ekwe_hub.zeepark.service;

import com.ekwe_hub.zeepark.event.ParkingSessionEndedEvent;
import com.ekwe_hub.zeepark.event.ParkingSessionStartedEvent;
import com.ekwe_hub.zeepark.exception.SpotUnavailableException;
import com.ekwe_hub.zeepark.model.parking.ParkingSession;
import com.ekwe_hub.zeepark.model.parking.ParkingSpot;
import com.ekwe_hub.zeepark.model.parking.SessionStatus;
import com.ekwe_hub.zeepark.repository.ParkingSessionRepository;
import com.ekwe_hub.zeepark.repository.ParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {

    private final ParkingSessionRepository parkingSessionRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public ParkingSession startSession(String vehicleId, String preferredSpotId) {
        // Prevent duplicate active sessions for the same vehicle
        boolean hasActiveSession = !parkingSessionRepository
                .findByVehicleIdAndStatus(vehicleId, SessionStatus.ACTIVE).isEmpty();
        if (hasActiveSession) {
            throw new IllegalStateException("Vehicle " + vehicleId + " already has an active parking session");
        }

        ParkingSpot spot;

        if (preferredSpotId != null) {
            spot = parkingSpotRepository.findById(preferredSpotId)
                    .orElseThrow(() -> new SpotUnavailableException("Preferred spot not found"));
            if (!spot.isAvailable()) {
                throw new SpotUnavailableException("Preferred spot is already occupied");
            }
        } else {
            List<ParkingSpot> availableSpots = parkingSpotRepository.findByAvailableTrue();
            if (availableSpots.isEmpty()) {
                throw new SpotUnavailableException("No parking spots available");
            }
            spot = availableSpots.get(0);
        }

        // Mark spot as occupied
        spot.setAvailable(false);
        parkingSpotRepository.save(spot);

        // Create parking session
        ParkingSession session = new ParkingSession();
        session.setVehicleId(vehicleId);
        session.setSpotId(spot.getId());
        session.setEntryTime(LocalDateTime.now());
        session.setStatus(SessionStatus.ACTIVE);
        ParkingSession savedSession = parkingSessionRepository.save(session);

        // Publish event
        eventPublisher.publishEvent(new ParkingSessionStartedEvent(
                savedSession.getId(),
                vehicleId,
                savedSession.getEntryTime()
        ));

        return savedSession;
    }

    @Override
    @Transactional
    public ParkingSession endSession(String sessionId) {
        ParkingSession session = parkingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found with ID: " + sessionId));

        if (session.getStatus() == SessionStatus.COMPLETED) {
            throw new IllegalStateException("Session is already completed");
        }

        session.setExitTime(LocalDateTime.now());
        session.setDuration((int) Duration.between(session.getEntryTime(), session.getExitTime()).toMinutes());
        session.setStatus(SessionStatus.COMPLETED);
        parkingSessionRepository.save(session);

        // Free the spot synchronously to avoid race conditions
        parkingSpotRepository.findById(session.getSpotId()).ifPresent(spot -> {
            spot.setAvailable(true);
            parkingSpotRepository.save(spot);
        });

        // Publish event for async listeners (notifications, etc.)
        eventPublisher.publishEvent(new ParkingSessionEndedEvent(
                session.getId(),
                session.getSpotId(),
                session.getExitTime(),
                session.getDuration()
        ));

        return session;
    }

    @Override
    public ParkingSession findActiveSessionByVehicleId(String vehicleId) {
        return parkingSessionRepository.findByVehicleIdAndStatus(vehicleId, SessionStatus.ACTIVE)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No active session for vehicle " + vehicleId));
    }
}