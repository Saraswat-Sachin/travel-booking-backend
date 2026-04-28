package com.travelbooking.bookingservice.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.travelbooking.bookingservice.dto.UserRegistrationRequest;
import com.travelbooking.bookingservice.entity.User;
import com.travelbooking.bookingservice.repository.UserRepository;
import com.travelbooking.bookingservice.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(UserRegistrationRequest request) {
        // 1. Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered.");
        }

        // 2. Map DTO -> Entity
        User user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
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
