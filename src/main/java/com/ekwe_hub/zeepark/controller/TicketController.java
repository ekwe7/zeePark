package com.ekwe_hub.zeepark.controller;

import com.ekwe_hub.zeepark.dto.request.TicketRequest;
import com.ekwe_hub.zeepark.dto.response.TicketResponse;
import com.ekwe_hub.zeepark.mapper.TicketMapper;
import com.ekwe_hub.zeepark.model.parking.Ticket;
import com.ekwe_hub.zeepark.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/search")
    public TicketResponse getTicketBySession(@Valid @RequestBody TicketRequest request) {
        Ticket ticket = ticketService.findBySessionId(request.sessionId());
        return TicketMapper.toDto(ticket);
    }

    @GetMapping
    public List<TicketResponse> getAllTickets() {
        return ticketService.findAll().stream()
                .map(TicketMapper::toDto)
                .toList();
    }
}