package com.ekwe_hub.zeepark.mapper;

import com.ekwe_hub.zeepark.dto.response.SpotCategoryResponse;
import com.ekwe_hub.zeepark.model.parking.SpotCategory;

public class SpotCategoryMapper {
    public static SpotCategoryResponse toDto(SpotCategory category) {
        return new SpotCategoryResponse(
                category.getId(),
                category.getType()
        );
    }
}
