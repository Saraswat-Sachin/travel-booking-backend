package com.travelbooking.bookingservice.service;

import com.travelbooking.bookingservice.dto.UserRegistrationRequest;

public interface UserService {
    void registerUser(UserRegistrationRequest request);
}
