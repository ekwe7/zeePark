package com.ekwe_hub.zeepark.service;

import com.ekwe_hub.zeepark.dto.request.LoginRequest;
import com.ekwe_hub.zeepark.dto.request.RegisterUserRequest;
import com.ekwe_hub.zeepark.dto.response.LoginResponse;
import com.ekwe_hub.zeepark.model.user.User;

import java.util.List;

public interface UserService {
    LoginResponse login(LoginRequest request);
    User registerUser(RegisterUserRequest request);
    User findById(String id);
    User findByUsername(String username);
    List<User> findAll();
    void deleteById(String id);
}