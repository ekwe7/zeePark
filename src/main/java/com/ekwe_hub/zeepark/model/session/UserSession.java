package com.ekwe_hub.zeepark.model.session;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Document(collection = "user_sessions")
public class UserSession {

    @Id
    private String token;

    private String userId;
    private String username;
    private String role;

    @Indexed
    private Instant expiresAt;

    private Instant lastActivity;

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}
