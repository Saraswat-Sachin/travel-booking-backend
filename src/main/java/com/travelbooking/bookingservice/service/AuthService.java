package com.travelbooking.bookingservice.service;

import com.travelbooking.bookingservice.dto.LoginRequest;
import com.travelbooking.bookingservice.dto.LoginResponse;
import com.travelbooking.bookingservice.dto.UserRegistrationRequest;
import com.travelbooking.bookingservice.entity.RefreshToken;

public interface AuthService {
    void registerUser(UserRegistrationRequest request);
    LoginResponse loginUser(LoginRequest request);
    void logoutUser(String email, RefreshToken token);
}
