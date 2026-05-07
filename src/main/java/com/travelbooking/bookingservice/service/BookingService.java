package com.travelbooking.bookingservice.service;

import com.travelbooking.bookingservice.dto.BookingResponse;
import com.travelbooking.bookingservice.dto.CreateBookingRequest;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface BookingService {
    BookingResponse createBooking(CreateBookingRequest request, String email);

    List<BookingResponse> getUserBookings(String email);

    void cancelBooking(Long bookingId, String email);
}
