package com.ekwe_hub.zeepark.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SpotCreateRequest(
        @NotBlank String zoneId,
        @NotBlank String categoryId
) {}