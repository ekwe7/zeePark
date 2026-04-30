package com.ekwe_hub.zeepark.repository;

import com.ekwe_hub.zeepark.model.parking.SpotCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpotCategoryRepository extends MongoRepository<SpotCategory, String> {
}
