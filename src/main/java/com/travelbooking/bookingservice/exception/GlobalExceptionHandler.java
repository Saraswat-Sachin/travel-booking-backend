package com.travelbooking.bookingservice.exception;

import com.travelbooking.bookingservice.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequest(BadRequestException ex){
        return ResponseEntity.badRequest().body(ApiResponse.builder().message(ex.getMessage()).data(null).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex){
        return ResponseEntity.internalServerError().body(ApiResponse.builder().message("Something went wrong.").data(null).build());
    }
}
