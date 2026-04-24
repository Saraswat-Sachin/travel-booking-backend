package com.travelbooking.bookingservice.service.service;

import com.travelbooking.bookingservice.dto.UserRegistrationRequest;

public interface UserService {
    void registerUser(UserRegistrationRequest request);
}
