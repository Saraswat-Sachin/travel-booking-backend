package com.travelbooking.bookingservice.service.impl;

import com.travelbooking.bookingservice.dto.BookingResponse;
import com.travelbooking.bookingservice.dto.CreateBookingRequest;
import com.travelbooking.bookingservice.dto.PageResponse;
import com.travelbooking.bookingservice.entity.Booking;
import com.travelbooking.bookingservice.entity.User;
import com.travelbooking.bookingservice.enums.BookingStatus;
import com.travelbooking.bookingservice.exception.BadRequestException;
import com.travelbooking.bookingservice.repository.BookingRepository;
import com.travelbooking.bookingservice.repository.UserRepository;
import com.travelbooking.bookingservice.service.BookingService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookingServiceImpl implements BookingService {
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    BookingServiceImpl(UserRepository userRepository, BookingRepository bookingRepository){
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;

    }
    @Override
    public BookingResponse createBooking(CreateBookingRequest request, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new BadRequestException("User not found"));
        Booking booking = Booking.builder()
                .user(user)
                .source(request.getSource())
                .destination(request.getDestination())
                .travelDate(request.getTravelDate())
                .numberOfPassengers(request.getNumberOfPassengers())
                .status(BookingStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();
        return mapToResponse(bookingRepository.save(booking));
    }

    @Override
    public PageResponse<BookingResponse> getAllBookings(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return PageResponse.from(bookingRepository.findAll(pageable)
                .map(this::mapToResponse));
    }

    @Override
    public PageResponse<BookingResponse> getUserBookingsByEmail(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return PageResponse.from(bookingRepository.findByUserEmail(email, pageable)
                .map(this::mapToResponse));
    }

    @Override
    public PageResponse<BookingResponse> getUserBookingsById(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return PageResponse.from(bookingRepository.findByUserId(userId, pageable)
                .map(this::mapToResponse));
    }

    @Override
    public BookingResponse getBookingById(long id) {
        return mapToResponse(bookingRepository.findById(id).orElseThrow(()->new BadRequestException("Booking not found")));
    }

    @Override
    public void cancelBooking(Long bookingId, String email) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BadRequestException("Booking not found"));
        if (!booking.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("You cannot cancel this booking");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .userId(booking.getUser().getId())
                .source(booking.getSource())
                .destination(booking.getDestination())
                .travelDate(booking.getTravelDate())
                .numberOfPassengers(booking.getNumberOfPassengers())
                .status(booking.getStatus())
                .build();
    }
}
