package com.travelbooking.bookingservice.service.service.impl;

import org.springframework.stereotype.Service;

import com.travelbooking.bookingservice.dto.UserRegistrationRequest;
import com.travelbooking.bookingservice.entity.User;
import com.travelbooking.bookingservice.repository.UserRepository;
import com.travelbooking.bookingservice.service.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void registerUser(UserRegistrationRequest request) {
        // 1. Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already regitered.");
        }

        // 2. Map DTO -> Entity
        User user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(request.getPassword()) // encrypt it later
                    .phoneNumber(request.getPhoneNumber())
                    .dateOfBirth(request.getDateOfBirth())
                    .gender(request.getGender())
                    .role("USER")
                    .status("ACTIVE")
                    .emailVerified(false)
                    .build();
        
        // 3. Save USER
        userRepository.save(user);
    }
    
}
