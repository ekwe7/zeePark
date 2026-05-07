package com.ekwe_hub.zeepark.service;

import com.ekwe_hub.zeepark.model.parking.Ticket;

import java.util.List;

public interface TicketService {
    Ticket findBySessionId(String sessionId);

    List<Ticket> findByCustomerId(String customerId);

    List<Ticket> findAll();
}