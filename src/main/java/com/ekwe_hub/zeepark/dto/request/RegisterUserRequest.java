package com.ekwe_hub.zeepark.dto.request;

import com.ekwe_hub.zeepark.model.user.UserRole;

public record RegisterUserRequest(String username, String password, String email, UserRole role) {
}
