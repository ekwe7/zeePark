package com.ekwe_hub.zeepark.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.ekwe_hub.zeepark.repository")
@EnableMongoAuditing
public class MongoConfig {
}