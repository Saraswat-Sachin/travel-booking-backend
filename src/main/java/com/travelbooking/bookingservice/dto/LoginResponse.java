package com.travelbooking.bookingservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    @Email
    @NotNull
    private String token;
    @NotNull
    private String message;
}
