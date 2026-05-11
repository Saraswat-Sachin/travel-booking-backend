package com.travelbooking.bookingservice.repository;

import com.travelbooking.bookingservice.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByUserEmail(String email, Pageable pageable);
    Page<Booking> findByUserId(Long userId, Pageable pageable);
}
