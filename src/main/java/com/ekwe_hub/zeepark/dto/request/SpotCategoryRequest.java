package com.ekwe_hub.zeepark.dto.request;

import com.ekwe_hub.zeepark.model.parking.SpotCategoryType;
import jakarta.validation.constraints.NotNull;

public record SpotCategoryRequest(
        @NotNull SpotCategoryType type
) {}
