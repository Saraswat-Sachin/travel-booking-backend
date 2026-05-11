package com.travelbooking.bookingservice.service;

import com.travelbooking.bookingservice.dto.*;

public interface UserService {

    UserResponse getUserById(Long id);

    UserResponse getUserByEmail(String email);

    UserResponse updateUserById(Long id, UserUpdateRequest request);
    UserResponse updateUserByEmail(String email, UserUpdateRequest request);

    void deleteUserById(Long id);
    void deleteUserByEmail(String email);
}
