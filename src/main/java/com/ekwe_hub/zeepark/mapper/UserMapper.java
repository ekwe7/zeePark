package com.ekwe_hub.zeepark.mapper;

import com.ekwe_hub.zeepark.dto.response.UserResponse;
import com.ekwe_hub.zeepark.model.user.User;

public class UserMapper {
    public static UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(),
                user.getUsername(), user.getEmail(), user.getRole());
    }
}
