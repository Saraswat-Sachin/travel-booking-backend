package com.travelbooking.bookingservice.service.impl;

import com.travelbooking.bookingservice.dto.BookingResponse;
import com.travelbooking.bookingservice.dto.CreateBookingRequest;
import com.travelbooking.bookingservice.entity.Booking;
import com.travelbooking.bookingservice.entity.User;
import com.travelbooking.bookingservice.exception.BadRequestException;
import com.travelbooking.bookingservice.repository.BookingRepository;
import com.travelbooking.bookingservice.repository.UserRepository;
import com.travelbooking.bookingservice.service.BookingService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
                .status("CREATED")
                .createdAt(LocalDateTime.now())
                .build();
        System.out.println("booking");
        System.out.println(booking.toString());
        return mapToResponse(bookingRepository.save(booking));
    }

    @Override
    public List<BookingResponse> getUserBookings(String email) {
        return bookingRepository.findByUserEmail(email)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void cancelBooking(Long bookingId, String email) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BadRequestException("Booking not found"));
        if (!booking.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("You cannot cancel this booking");
        }
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
    }

    private BookingResponse mapToResponse(Booking booking) {
        System.out.println("returing");
        System.out.println(BookingResponse.builder()
                .id(booking.getId())
                .source(booking.getSource())
                .destination(booking.getDestination())
                .travelDate(booking.getTravelDate())
                .numberOfPassengers(booking.getNumberOfPassengers())
                .status(booking.getStatus())
                .build());
        return BookingResponse.builder()
                .id(booking.getId())
                .source(booking.getSource())
                .destination(booking.getDestination())
                .travelDate(booking.getTravelDate())
                .numberOfPassengers(booking.getNumberOfPassengers())
                .status(booking.getStatus())
                .build();
    }
}
