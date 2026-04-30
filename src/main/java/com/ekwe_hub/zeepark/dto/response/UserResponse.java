package com.ekwe_hub.zeepark.dto.response;

import com.ekwe_hub.zeepark.model.user.UserRole;

public record UserResponse(String id, String username, String email, UserRole role) {
}
