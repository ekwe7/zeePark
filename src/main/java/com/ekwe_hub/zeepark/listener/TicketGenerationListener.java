package com.ekwe_hub.zeepark.listener;

import com.ekwe_hub.zeepark.event.ParkingSessionStartedEvent;
import com.ekwe_hub.zeepark.model.parking.Ticket;
import com.ekwe_hub.zeepark.model.vehicle.Vehicle;
import com.ekwe_hub.zeepark.repository.TicketRepository;
import com.ekwe_hub.zeepark.repository.VehicleRepository;
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
    private final VehicleRepository vehicleRepository;

    @Async
    @EventListener
    public void onSessionStarted(ParkingSessionStartedEvent event) {
        Ticket ticket = new Ticket();
        ticket.setSessionId(event.sessionId());
        ticket.setIssuedAt(event.entryTime());

        // Attach vehicle number plate to ticket
        vehicleRepository.findById(event.vehicleId()).ifPresent(vehicle ->
                ticket.setVehicleNumberPlate(vehicle.getNumberPlate())
        );

        ticketRepository.save(ticket);
        log.info("Ticket generated for session {} vehicle {}", event.sessionId(), ticket.getVehicleNumberPlate());
    }
}
