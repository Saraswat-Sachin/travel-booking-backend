package com.travelbooking.bookingservice.controller.auth;

import com.travelbooking.bookingservice.util.JwtUtil;
import com.travelbooking.bookingservice.dto.ApiResponse;
import com.travelbooking.bookingservice.dto.LoginRequest;
import com.travelbooking.bookingservice.dto.LoginResponse;
import com.travelbooking.bookingservice.dto.UserRegistrationRequest;
import com.travelbooking.bookingservice.entity.RefreshToken;
import com.travelbooking.bookingservice.service.AuthService;
import com.travelbooking.bookingservice.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/auth")
@EnableMethodSecurity
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    @Value("${jwt.refresh-token.expiration}")
    private long REFRESH_EXPIRATION;

    AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> registerUser(
            @Valid @RequestBody UserRegistrationRequest request) {
        authService.registerUser(request);
        return ResponseEntity.ok(new ApiResponse<>("User registered successfully.", null));
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> loginUser(
            @Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        LoginResponse auth = authService.loginUser(request);
        String refreshToken = refreshTokenService.createRefreshToken(request.getEmail()).getToken();
        // setting in http cookies
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // will fix moving in prod
                .path("/")
                .maxAge(Duration.ofSeconds(REFRESH_EXPIRATION))
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        LoginResponse final_response = LoginResponse.builder()
                .accessToken(auth.getAccessToken())
                .build();
        return ResponseEntity.ok(new ApiResponse<>("User logged in successfully.", final_response));
    }
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Object>> refreshToken(
            @CookieValue(name = "refreshToken") String refreshToken, HttpServletResponse response) {
        RefreshToken oldRefreshToken = refreshTokenService.getValidToken(refreshToken);
        RefreshToken newRefreshToken = refreshTokenService.rotateToken(oldRefreshToken);
        String newAccessToken = jwtUtil.generateToken(newRefreshToken.getUser().getEmail());

        // setting in http cookies
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", newRefreshToken.getToken())
                .httpOnly(true)
                .secure(false) // will fix moving in prod
                .path("/")
                .maxAge(Duration.ofSeconds(REFRESH_EXPIRATION))
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        LoginResponse final_response = LoginResponse.builder()
                .accessToken(newAccessToken)
                .build();
        return ResponseEntity.ok(new ApiResponse<>("Access token refreshed in successfully.", final_response));
    }
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(@CookieValue(name = "refreshToken") String refreshToken, HttpServletResponse response) {
        refreshTokenService.removeToken(refreshToken);
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false) // will fix moving in prod
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        return ResponseEntity.ok(new ApiResponse<>("User logged out successfully.", null));
    }
}
