package com.ekwe_hub.zeepark.service;

import com.ekwe_hub.zeepark.exception.UnauthorizedException;
import com.ekwe_hub.zeepark.model.user.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemorySessionService implements SessionService {

    private final Map<String, User> activeSessions = new ConcurrentHashMap<>();

    @Override
    public String createSession(User user) {
        String token = UUID.randomUUID().toString();
        activeSessions.put(token, user);
        return token;
    }

    @Override
    public void invalidateSession(String token) {
        activeSessions.remove(token);
    }

    @Override
    public User getAuthenticatedUser(String token) {
        User user = activeSessions.get(token);
        if (user == null) {
            throw new UnauthorizedException("Invalid or expired session");
        }
        return user;
    }
}