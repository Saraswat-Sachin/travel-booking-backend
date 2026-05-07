package com.travelbooking.bookingservice.controller;

import com.travelbooking.bookingservice.dto.ApiResponse;
import com.travelbooking.bookingservice.dto.BookingResponse;
import com.travelbooking.bookingservice.dto.CreateBookingRequest;
import com.travelbooking.bookingservice.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {
    private final BookingService bookingService;
    BookingController(BookingService bookingService){
        this.bookingService = bookingService;
    }
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createBooking(@Valid @RequestBody CreateBookingRequest request, Authentication authentication){
        String email = authentication.getName();

        return ResponseEntity.ok(ApiResponse.builder().message("Booking created").data(bookingService.createBooking(request, email)).build());
    }
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getMyBookings(Authentication authentication){
        String email = authentication.getName();
        return ResponseEntity.ok(ApiResponse.<List<BookingResponse>>builder().message("Booking fetched").data(bookingService.getUserBookings(email)).build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> cancelBooking(@PathVariable long id, Authentication authentication){
        String email = authentication.getName();

        bookingService.cancelBooking(id, email);
        return ResponseEntity.ok(ApiResponse.builder().message("Booking deleted successfully").build());
    }
}
