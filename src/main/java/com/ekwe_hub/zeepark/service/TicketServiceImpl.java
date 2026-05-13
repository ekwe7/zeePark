package com.ekwe_hub.zeepark.service;

import com.ekwe_hub.zeepark.exception.ResourceNotFoundException;
import com.ekwe_hub.zeepark.model.parking.Ticket;
import com.ekwe_hub.zeepark.model.user.Customer;
import com.ekwe_hub.zeepark.repository.ParkingSessionRepository;
import com.ekwe_hub.zeepark.repository.TicketRepository;
import com.ekwe_hub.zeepark.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final ParkingSessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Override
    public Ticket findBySessionId(String sessionId) {
        return ticketRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found for session: " + sessionId));
    }

    @Override
    public List<Ticket> findByCustomerId(String customerId) {
        return userRepository.findById(customerId)
                .filter(u -> u instanceof Customer)
                .map(u -> {
                    List<String> vehicleIds = ((Customer) u).getVehicles().stream()
                            .map(v -> v.getId()).toList();
                    List<String> sessionIds = sessionRepository.findByVehicleIdIn(vehicleIds).stream()
                            .map(s -> s.getUserId()).toList();
                    return ticketRepository.findBySessionIdIn(sessionIds);
                })
                .orElse(List.of());
    }

    @Override
    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }
}