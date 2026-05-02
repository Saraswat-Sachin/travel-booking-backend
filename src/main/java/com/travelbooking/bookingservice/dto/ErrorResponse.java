package com.travelbooking.bookingservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private int status;              // HTTP status code
    private String error;            // Short error type
    private String message;          // Detailed message
    private String path;             // API path
    private LocalDateTime timestamp; // When error occurred
}
