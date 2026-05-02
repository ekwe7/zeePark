package com.ekwe_hub.zeepark.listener;

import com.ekwe_hub.zeepark.event.ParkingSessionStartedEvent;
import com.ekwe_hub.zeepark.model.parking.Ticket;
import com.ekwe_hub.zeepark.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketGenerationListener {

    private final TicketRepository ticketRepository;

    @Async
    @EventListener
    public void onSessionStarted(ParkingSessionStartedEvent event) {
        Ticket ticket = new Ticket();
        ticket.setSessionId(event.sessionId());
        ticket.setIssuedAt(event.entryTime());
        ticketRepository.save(ticket);
        log.info("Ticket generated for session {}", event.sessionId());
    }
}