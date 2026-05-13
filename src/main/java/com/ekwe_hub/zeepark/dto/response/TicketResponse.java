package com.ekwe_hub.zeepark.dto.response;

import java.time.LocalDateTime;

public record TicketResponse(
                String id,
                String sessionId,
                String vehicleNumberPlate,
                LocalDateTime issuedAt) {
}
