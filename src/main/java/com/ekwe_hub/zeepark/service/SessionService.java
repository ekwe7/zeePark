package com.ekwe_hub.zeepark.service;

import com.ekwe_hub.zeepark.model.user.User;

public interface SessionService {
    String createSession(User user);
    void invalidateSession(String token);
    User getAuthenticatedUser(String token);
}
