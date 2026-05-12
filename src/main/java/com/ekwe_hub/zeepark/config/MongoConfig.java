package com.ekwe_hub.zeepark.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.time.Duration;

@Configuration
@EnableMongoRepositories(basePackages = "com.ekwe_hub.zeepark.repository")
@EnableMongoAuditing
@RequiredArgsConstructor
public class MongoConfig {

    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void createTtlIndex() {
        try {
            // TTL index on user_sessions.expiresAt — MongoDB auto-deletes expired sessions
            mongoTemplate.indexOps("user_sessions")
                    .createIndex(new Index()
                            .on("expiresAt", Sort.Direction.ASC)
                            .expire(Duration.ZERO)
                            .named("expiresAt_ttl"));
        } catch (Exception e) {
            // Index already exists — safe to ignore
        }
    }
}