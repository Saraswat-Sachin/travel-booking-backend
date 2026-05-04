package com.travelbooking.bookingservice.service;

import com.travelbooking.bookingservice.entity.RefreshToken;
import com.travelbooking.bookingservice.entity.User;
import com.travelbooking.bookingservice.repository.RefreshTokenRepository;
import com.travelbooking.bookingservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.refresh-token.expiration}")
    private long REFRESH_EXPIRATION ;
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository){
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public RefreshToken createRefreshToken(String email){
        User user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found"));

        refreshTokenRepository.deleteByUser(user);

        RefreshToken token = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiry(Instant.now().plusSeconds(REFRESH_EXPIRATION))
                .build();

        return refreshTokenRepository.save(token);
    }
    public RefreshToken getValidToken(String token){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(()-> new RuntimeException("Invalid refresh token"));

        if (refreshToken.getExpiry().isBefore(Instant.now())){
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }
        return refreshToken;
    }
    public RefreshToken rotateToken(RefreshToken oldToken){
        User user = oldToken.getUser();

        refreshTokenRepository.delete(oldToken);

        return createRefreshToken(user.getEmail());
    }
    @Transactional
    public void removeToken(String oldToken){
        refreshTokenRepository.deleteByToken(oldToken);
    }
}
