package com.example.todoproject.user.service;

import com.example.todoproject.common.dto.TokenResponse;
import com.example.todoproject.common.jwt.service.JwtService;
import com.example.todoproject.redis.RefreshToken;
import com.example.todoproject.redis.RefreshTokenRepository;
import com.example.todoproject.todo.dto.RefreshTokenDto;
import com.example.todoproject.user.domain.User;
import com.example.todoproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenResponse issueToken(String token) {
        String email = jwtService.extractEmail(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("1번 후보"));
        return jwtService.toTokenResponse(user.getId());
    }

    public TokenResponse reIssueToken(RefreshTokenDto refreshTokenDto) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenDto.value())
                .orElseThrow(() -> new IllegalArgumentException("2번 후보"));
        return jwtService.toTokenResponse(refreshToken.getUserId());
    }
}
