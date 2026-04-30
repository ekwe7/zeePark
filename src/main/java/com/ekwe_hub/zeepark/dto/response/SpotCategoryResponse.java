package com.ekwe_hub.zeepark.dto.response;

import com.ekwe_hub.zeepark.model.parking.SpotCategoryType;

public record SpotCategoryResponse(
        String id,
        SpotCategoryType type
) {}
