package com.example.blog.service;

import com.example.blog.config.jwt.TokenProvider;
import com.example.blog.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String crateNewAccessToken(String refreshToken) {
        if (!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected Token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        Users user = userService.findById(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2)); // 2시간 후에 만료되는 액세스 토큰 생성
    }
}
