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
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {

        UserResponse user = userService.getUserById(id);

        return ResponseEntity.ok(new ApiResponse<UserResponse>("User fetched successfully.", user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest request) {

        UserResponse updatedUser = userService.updateUserById(id, request);
        return ResponseEntity.ok(new ApiResponse<UserResponse>("User updated successfully.", updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable Long id) {

        userService.deleteUserById(id);
        return ResponseEntity.ok(new ApiResponse<>("User deleted successfully.", null));
    }
}
