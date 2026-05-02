package com.travelbooking.bookingservice.service.impl;

import com.travelbooking.bookingservice.config.JwtUtil;
import com.travelbooking.bookingservice.dto.LoginRequest;
import com.travelbooking.bookingservice.dto.LoginResponse;
import com.travelbooking.bookingservice.exception.BadRequestException;
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
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void registerUser(UserRegistrationRequest request) {
        // 1. Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered.");
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

    @Override
    public LoginResponse loginUser(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new BadRequestException("Invalid credentials"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new BadRequestException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(request.getEmail());
        return LoginResponse.builder()
                .token(token)
                .message("Login successful")
                .build();
    }
}
