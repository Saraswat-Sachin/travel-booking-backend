package com.travelbooking.bookingservice.controller;

import com.travelbooking.bookingservice.dto.ApiResponse;
import com.travelbooking.bookingservice.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import com.travelbooking.bookingservice.dto.UserRegistrationRequest;
import com.travelbooking.bookingservice.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@EnableMethodSecurity
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> registerUser(
            @Valid @RequestBody UserRegistrationRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok(ApiResponse.builder().message("User registered successfully.").build());
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> loginUser(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.builder().message("User registered successfully.").data(userService.loginUser(request)).build());
    }

    // testing purpose - will delete later
    @GetMapping("/hehe")
    public ResponseEntity<ApiResponse<Object>> hehe() {
        return ResponseEntity.ok(ApiResponse.builder().message("User authenticated successfully.").build());
    }
    @GetMapping("/secured-hehe")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> secured_hehe() {
        return ResponseEntity.ok(ApiResponse.builder().message("Admin authenticated successfully.").build());
    }
}
