package com.travelbooking.bookingservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateBookingRequest {
    private String source;
    private String destination;
    private LocalDate travelDate;
    private int numberOfPassengers;
}
