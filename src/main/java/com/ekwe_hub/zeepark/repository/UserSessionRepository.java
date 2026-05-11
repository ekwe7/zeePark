package com.ekwe_hub.zeepark.repository;

import com.ekwe_hub.zeepark.model.session.UserSession;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserSessionRepository extends MongoRepository<UserSession, String> {
    Optional<UserSession> findByToken(String token);

    void deleteByUserId(String userId);
}
