package com.travelbooking.bookingservice.service;

import com.travelbooking.bookingservice.dto.BookingResponse;
import com.travelbooking.bookingservice.dto.CreateBookingRequest;
import com.travelbooking.bookingservice.dto.PageResponse;

public interface BookingService {
    BookingResponse createBooking(CreateBookingRequest request, String email);

    PageResponse<BookingResponse> getAllBookings(int page, int size);

    PageResponse<BookingResponse> getUserBookingsByEmail(String email, int page, int size);

    PageResponse<BookingResponse> getUserBookingsById(Long userId, int page, int size);

    BookingResponse getBookingById(long id);

    void cancelBooking(Long bookingId, String email);
}
