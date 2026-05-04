package com.travelbooking.bookingservice.repository;

import com.travelbooking.bookingservice.entity.RefreshToken;
import com.travelbooking.bookingservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String Token);

    void deleteByUser(User user);

    void deleteByToken(String oldToken);
}
