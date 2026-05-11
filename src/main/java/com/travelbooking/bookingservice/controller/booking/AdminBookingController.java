package com.travelbooking.bookingservice.controller.booking;

import com.travelbooking.bookingservice.dto.ApiResponse;
import com.travelbooking.bookingservice.dto.BookingResponse;
import com.travelbooking.bookingservice.dto.PageResponse;
import com.travelbooking.bookingservice.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBookingController {
    private final BookingService bookingService;
    AdminBookingController(BookingService bookingService){
        this.bookingService = bookingService;
    }
    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<PageResponse<BookingResponse>>> getAllBookings(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(new ApiResponse<PageResponse<BookingResponse>>("Bookings fetched.", bookingService.getAllBookings(page, size)));
    }
    @GetMapping("/bookings/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getMyBooking(@PathVariable long id, Authentication authentication){
        String email = authentication.getName();
        return ResponseEntity.ok(new ApiResponse<BookingResponse>("Booking fetched.", bookingService.getBookingById(id)));
    }
    @PatchMapping("/bookings/{id}/cancel")
    public ResponseEntity<ApiResponse<Object>> cancelBooking(@PathVariable long id, Authentication authentication){
        String email = authentication.getName();

        bookingService.cancelBooking(id, email);
        return ResponseEntity.ok(new ApiResponse<>("Booking deleted successfully.", null));
    }


    @GetMapping("/users/{userId}/bookings")
    public ResponseEntity<ApiResponse<PageResponse<BookingResponse>>> getUserBookings(@PathVariable Long userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(new ApiResponse<PageResponse<BookingResponse>>("Bookings fetched.", bookingService.getUserBookingsById(userId, page, size)));
    }
}
