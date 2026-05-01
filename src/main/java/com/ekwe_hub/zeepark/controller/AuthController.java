package com.ekwe_hub.zeepark.controller;

import com.ekwe_hub.zeepark.dto.request.LoginRequest;
import com.ekwe_hub.zeepark.dto.response.LoginResponse;
import com.ekwe_hub.zeepark.service.SessionService;
import com.ekwe_hub.zeepark.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final SessionService sessionService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpSession httpSession) {
        LoginResponse response = userService.login(request);
        httpSession.setAttribute("token", response.token());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession httpSession) {
        String token = (String) httpSession.getAttribute("token");
        if (token != null) {
            sessionService.invalidateSession(token);
        }
        httpSession.invalidate();
        return ResponseEntity.noContent().build();
    }
}
