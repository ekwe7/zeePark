package com.ekwe_hub.zeepark.service;

import com.ekwe_hub.zeepark.dto.request.LoginRequest;
import com.ekwe_hub.zeepark.dto.request.RegisterUserRequest;
import com.ekwe_hub.zeepark.dto.response.LoginResponse;
import com.ekwe_hub.zeepark.exception.ResourceNotFoundException;
import com.ekwe_hub.zeepark.exception.UnauthorizedException;
import com.ekwe_hub.zeepark.model.user.*;
import com.ekwe_hub.zeepark.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        String token = sessionService.createSession(user);
        return new LoginResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), token);
    }

    @Override
    public User registerUser(RegisterUserRequest request) {
        String hashedPassword = passwordEncoder.encode(request.password());
        User user = switch (request.role()) {
            case ADMIN -> new Admin(request.username(), hashedPassword, request.email());
            case CUSTOMER -> new Customer(request.username(), hashedPassword, request.email());
            case STAFF -> new Staff(request.username(), hashedPassword, request.email());
        };
        return userRepository.save(user);
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        User user = findById(id);
        userRepository.delete(user);
    }
}
