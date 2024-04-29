package com.example.todoproject.user.service;

import com.example.todoproject.auth.jwt.service.JwtService;
import com.example.todoproject.common.dto.TokenResponse;
import com.example.todoproject.redis.RefreshToken;
import com.example.todoproject.redis.RefreshTokenRepository;
import com.example.todoproject.todo.dto.RefreshTokenDto;
import com.example.todoproject.user.domain.User;
import com.example.todoproject.user.dto.MyPageDto;
import com.example.todoproject.user.dto.RedisDto;
import com.example.todoproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenResponse issueToken(String token) {
        String email = jwtService.extractEmail(token);
        return jwtService.toTokenResponse(email);
    }

    @Transactional
    public TokenResponse issue(RedisDto redisDto) {
        return jwtService.toTokenResponse(redisDto.email());
    }

    @Transactional
    public TokenResponse reIssueToken(RefreshTokenDto refreshTokenDto) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenDto.value())
                .orElseThrow(() -> new IllegalArgumentException("2번 후보"));
        return jwtService.toTokenResponse(refreshToken.getEmail());
    }

    public MyPageDto getMyPage(String email) {
        User user = getUser(email);
        return new MyPageDto(user.getName(), user.getEmail());
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
    }
}
