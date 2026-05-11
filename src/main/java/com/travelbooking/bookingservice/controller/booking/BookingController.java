package com.travelbooking.bookingservice.controller.booking;

import com.travelbooking.bookingservice.dto.ApiResponse;
import com.travelbooking.bookingservice.dto.BookingResponse;
import com.travelbooking.bookingservice.dto.CreateBookingRequest;
import com.travelbooking.bookingservice.dto.PageResponse;
import com.travelbooking.bookingservice.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(new ApiResponse<>("Booking created.", bookingService.createBooking(request, email)));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BookingResponse>>> getAllBookings(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, Authentication authentication){
        String email = authentication.getName();
        return ResponseEntity.ok(new ApiResponse<PageResponse<BookingResponse>>("Booking fetched.", bookingService.getUserBookingsByEmail(email, page, size)));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getMyBooking(@PathVariable long id, Authentication authentication){
        String email = authentication.getName();
        return ResponseEntity.ok(new ApiResponse<BookingResponse>("Booking fetched.", bookingService.getBookingById(id)));
    }
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Object>> cancelBooking(@PathVariable long id, Authentication authentication){
        String email = authentication.getName();

        bookingService.cancelBooking(id, email);
        return ResponseEntity.ok(new ApiResponse<>("Booking deleted successfully.", null));
    }
}
