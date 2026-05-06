package com.ekwe_hub.zeepark.service;

import com.ekwe_hub.zeepark.exception.ResourceNotFoundException;
import com.ekwe_hub.zeepark.model.parking.Ticket;
import com.ekwe_hub.zeepark.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    @Override
    public Ticket findBySessionId(String sessionId) {
        return ticketRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found for session: " + sessionId));
    }

    @Override
    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }
}