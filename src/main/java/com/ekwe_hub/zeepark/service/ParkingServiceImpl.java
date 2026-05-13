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
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
    private final MongoTemplate mongoTemplate;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public ParkingSession startSession(String userId, String vehicleId, String preferredSpotId) {
        List<ParkingSession> vehicleActiveSessions = parkingSessionRepository
                .findByVehicleIdAndStatus(vehicleId, SessionStatus.ACTIVE);
        if (!vehicleActiveSessions.isEmpty()) {
            ParkingSession existingSession = vehicleActiveSessions.get(0);
            if (!existingSession.getUserId().equals(userId)) {
                throw new IllegalStateException("Vehicle " + vehicleId + " already has an active parking session");
            }
            return existingSession;
        }

        List<ParkingSession> activeUserSessions = parkingSessionRepository
                .findByUserIdAndStatus(userId, SessionStatus.ACTIVE);
        if (!activeUserSessions.isEmpty()) {
            return activeUserSessions.get(0);
        }

        ParkingSpot spot;
        if (preferredSpotId != null) {
            spot = reservePreferredSpot(preferredSpotId);
        } else {
            spot = reserveAnyAvailableSpot();
        }

        // Create parking session
        ParkingSession session = new ParkingSession();
        session.setUserId(userId);
        session.setVehicleId(vehicleId);
        session.setSpotId(spot.getId());
        session.setEntryTime(LocalDateTime.now());
        session.setStatus(SessionStatus.ACTIVE);
        ParkingSession savedSession = parkingSessionRepository.save(session);

        // Publish event
        eventPublisher.publishEvent(new ParkingSessionStartedEvent(
                savedSession.getUserId(),
                vehicleId,
                savedSession.getEntryTime()));

        return savedSession;
    }

    private ParkingSpot reservePreferredSpot(String preferredSpotId) {
        Query query = new Query(Criteria.where("id").is(preferredSpotId).and("available").is(true));
        Update update = new Update().set("available", false);
        ParkingSpot reservedSpot = mongoTemplate.findAndModify(query, update,
                FindAndModifyOptions.options().returnNew(true), ParkingSpot.class);
        if (reservedSpot == null) {
            throw new SpotUnavailableException("Preferred spot not found or already occupied");
        }
        return reservedSpot;
    }

    private ParkingSpot reserveAnyAvailableSpot() {
        Query query = new Query(Criteria.where("available").is(true));
        Update update = new Update().set("available", false);
        ParkingSpot reservedSpot = mongoTemplate.findAndModify(query, update,
                FindAndModifyOptions.options().returnNew(true), ParkingSpot.class);
        if (reservedSpot == null) {
            throw new SpotUnavailableException("No parking spots available");
        }
        return reservedSpot;
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
                session.getUserId(),
                session.getSpotId(),
                session.getExitTime(),
                session.getDuration()));

        return session;
    }

    @Override
    public ParkingSession findActiveSessionByUserId(String userId) {
        return parkingSessionRepository.findByUserIdAndStatus(userId, SessionStatus.ACTIVE)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No active session for user " + userId));
    }

    @Override
    public ParkingSession findActiveSessionByVehicleId(String vehicleId) {
        return parkingSessionRepository.findByVehicleIdAndStatus(vehicleId, SessionStatus.ACTIVE)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No active session for vehicle " + vehicleId));
    }
}