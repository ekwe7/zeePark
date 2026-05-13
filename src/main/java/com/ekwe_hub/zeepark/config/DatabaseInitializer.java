package com.ekwe_hub.zeepark.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.PartialIndexFilter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer {

    private final MongoTemplate mongoTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeIndexes() {
        log.info("Creating MongoDB indexes after application startup...");
        createUserSessionTtlIndex();
        createParkingSessionActiveIndexes();
        log.info("MongoDB index initialization complete.");
    }

    private void createUserSessionTtlIndex() {
        try {
            mongoTemplate.indexOps("user_sessions")
                    .createIndex(new Index()
                            .on("expiresAt", Sort.Direction.ASC)
                            .expire(Duration.ZERO)
                            .named("expiresAt_ttl"));
        } catch (Exception ignored) {
            // Index may already exist or Mongo is not ready yet.
        }
    }

    private void createParkingSessionActiveIndexes() {
        try {
            mongoTemplate.indexOps("Parking_Session")
                    .createIndex(new Index()
                            .on("vehicleId", Sort.Direction.ASC)
                            .unique()
                            .partial(PartialIndexFilter.of(Criteria.where("status").is("ACTIVE")))
                            .named("unique_active_vehicle"));

            mongoTemplate.indexOps("Parking_Session")
                    .createIndex(new Index()
                            .on("userId", Sort.Direction.ASC)
                            .unique()
                            .partial(PartialIndexFilter.of(Criteria.where("status").is("ACTIVE")))
                            .named("unique_active_user"));
        } catch (Exception ignored) {
            // Index may already exist or Mongo is not ready yet.
        }
    }
}
