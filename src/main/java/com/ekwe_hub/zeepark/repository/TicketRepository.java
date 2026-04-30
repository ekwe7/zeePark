package com.ekwe_hub.zeepark.repository;

import com.ekwe_hub.zeepark.model.parking.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    Optional<Ticket> findBySessionId(String sessionId);
}
