package com.ekwe_hub.zeepark.service;

import com.ekwe_hub.zeepark.exception.UnauthorizedException;
import com.ekwe_hub.zeepark.model.user.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemorySessionService implements SessionService {

    private static final long SESSION_TIMEOUT_MS = 2 * 60 * 1000;

    private record SessionEntry(User user, Instant lastActivity) {}

    private final Map<String, SessionEntry> activeSessions = new ConcurrentHashMap<>();

    @Override
    public String createSession(User user) {
        String token = UUID.randomUUID().toString();
        activeSessions.put(token, new SessionEntry(user, Instant.now()));
        return token;
    }

    @Override
    public void invalidateSession(String token) {
        activeSessions.remove(token);
    }

    @Override
    public User getAuthenticatedUser(String token) {
        SessionEntry entry = activeSessions.get(token);
        if (entry == null) {
            throw new UnauthorizedException("Invalid or expired session");
        }
        // Check if session has timed out
        if (Instant.now().toEpochMilli() - entry.lastActivity().toEpochMilli() > SESSION_TIMEOUT_MS) {
            activeSessions.remove(token);
            throw new UnauthorizedException("Session expired due to inactivity");
        }
        // Refresh last activity on each use
        activeSessions.put(token, new SessionEntry(entry.user(), Instant.now()));
        return entry.user();
    }
}
