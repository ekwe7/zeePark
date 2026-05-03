package com.ekwe_hub.zeepark.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TicketRequest(
        @NotBlank String sessionId
) {}
