package com.example.todoproject.user.service;

import com.example.todoproject.common.dto.TokenResponse;
import com.example.todoproject.common.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final JwtService jwtService;

    public TokenResponse issueToken(String token) {
        String email = jwtService.extractEmail(token);
        return jwtService.toTokenResponse(email);
    }
}
