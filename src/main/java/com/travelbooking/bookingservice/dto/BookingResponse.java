package com.travelbooking.bookingservice.dto;

import com.travelbooking.bookingservice.enums.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class BookingResponse {
    private Long id;
    private Long userId;
    private String source;
    private String destination;
    private LocalDate travelDate;
    private int numberOfPassengers;
    private BookingStatus status;
}
