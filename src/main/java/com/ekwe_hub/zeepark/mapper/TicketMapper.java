package com.ekwe_hub.zeepark.mapper;

import com.ekwe_hub.zeepark.dto.response.TicketResponse;
import com.ekwe_hub.zeepark.model.parking.Ticket;

public class TicketMapper {
    public static TicketResponse toDto(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getSessionId(),
                ticket.getIssuedAt()
        );

    }

}
