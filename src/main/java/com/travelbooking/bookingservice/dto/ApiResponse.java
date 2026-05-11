package com.travelbooking.bookingservice.dto;

public record ApiResponse<T>(String message, T data) {
}
