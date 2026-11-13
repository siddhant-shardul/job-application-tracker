package com.siddhant.backend.auth.controller;

import com.siddhant.backend.auth.dto.AuthResponse;
import com.siddhant.backend.auth.dto.LoginRequest;
import com.siddhant.backend.auth.dto.RegisterRequest;
import com.siddhant.backend.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(
            @Valid @RequestBody RegisterRequest request) {

        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request) {

        return authService.login(request);
    }
    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        var user = authService.getUser(authentication.getName());
        return Map.of("id", user.getId(), "fullName", user.getFullName(), "email", user.getEmail(), "role", user.getRole().name());
    }
}