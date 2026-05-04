package com.travelbooking.bookingservice.service;

import com.travelbooking.bookingservice.dto.*;

public interface UserService {

    UserResponse getUserById(Long id);

    UserResponse getUserByEmail(String email);

    UserResponse updateUser(Long id, UserUpdateRequest request);

    void deleteUser(Long id);
}
