package com.travelbooking.bookingservice.controller.user;

import com.travelbooking.bookingservice.dto.ApiResponse;
import com.travelbooking.bookingservice.dto.UserResponse;
import com.travelbooking.bookingservice.dto.UserUpdateRequest;
import com.travelbooking.bookingservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(Authentication authentication) {

        String email = authentication.getName();

        UserResponse user = userService.getUserByEmail(email);
        return ResponseEntity.ok(new ApiResponse<UserResponse>("User fetched successfully.", user));
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            Authentication authentication,
            @RequestBody UserUpdateRequest request) {
        String email = authentication.getName();
        UserResponse updatedUser = userService.updateUserByEmail(email, request);
        return ResponseEntity.ok(new ApiResponse<UserResponse>("User updated successfully.", updatedUser));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Object>> deleteUser(Authentication authentication) {
        String email = authentication.getName();
        userService.deleteUserByEmail(email);
        return ResponseEntity.ok(new ApiResponse<>("User deleted successfully.", null));
    }
}
