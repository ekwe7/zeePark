package com.ekwe_hub.zeepark.controller;

import com.ekwe_hub.zeepark.dto.request.RegisterUserRequest;
import com.ekwe_hub.zeepark.dto.response.UserResponse;
import com.ekwe_hub.zeepark.mapper.UserMapper;
import com.ekwe_hub.zeepark.model.user.User;
import com.ekwe_hub.zeepark.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerUser(@Valid @RequestBody RegisterUserRequest request) {
        User user = userService.registerUser(request);
        return UserMapper.toUserResponse(user);
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable String id) {
        User user = userService.findById(id);
        return UserMapper.toUserResponse(user);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.findAll().stream()
                .map(UserMapper::toUserResponse)
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String id) {
        userService.deleteById(id);
    }
}