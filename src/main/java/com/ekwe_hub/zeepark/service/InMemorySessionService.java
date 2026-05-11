package com.ekwe_hub.zeepark.service;

import com.ekwe_hub.zeepark.exception.UnauthorizedException;
import com.ekwe_hub.zeepark.model.session.UserSession;
import com.ekwe_hub.zeepark.model.user.User;
import com.ekwe_hub.zeepark.repository.UserRepository;
import com.ekwe_hub.zeepark.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InMemorySessionService implements SessionService {

    private static final long SESSION_TIMEOUT_SECONDS = 2 * 60; // 2 minutes

    private final UserSessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Override
    public String createSession(User user) {
        // Remove any existing sessions for this user
        sessionRepository.deleteByUserId(user.getId());

        String token = UUID.randomUUID().toString();
        UserSession session = new UserSession();
        session.setToken(token);
        session.setUserId(user.getId());
        session.setUsername(user.getUsername());
        session.setRole(user.getRole().name());
        session.setLastActivity(Instant.now());
        session.setExpiresAt(Instant.now().plusSeconds(SESSION_TIMEOUT_SECONDS));
        sessionRepository.save(session);
        return token;
    }

    @Override
    public void invalidateSession(String token) {
        sessionRepository.deleteById(token);
    }

    @Override
    public User getAuthenticatedUser(String token) {
        UserSession session = sessionRepository.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Invalid or expired session"));

        if (session.isExpired()) {
            sessionRepository.deleteById(token);
            throw new UnauthorizedException("Session expired due to inactivity");
        }

        // Refresh expiry on activity
        session.setLastActivity(Instant.now());
        session.setExpiresAt(Instant.now().plusSeconds(SESSION_TIMEOUT_SECONDS));
        sessionRepository.save(session);

        return userRepository.findById(session.getUserId())
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }
}
