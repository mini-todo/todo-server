package com.example.todoproject.user.service;

import com.example.todoproject.common.dto.TokenResponse;
import com.example.todoproject.common.jwt.service.JwtService;
import com.example.todoproject.todo.dto.RefreshToken;
import com.example.todoproject.user.domain.User;
import com.example.todoproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public TokenResponse issueToken(String token) {
        String email = jwtService.extractEmail(token);
        return jwtService.toTokenResponse(email);
    }

    public TokenResponse reIssueToken(RefreshToken refreshToken) {
        System.out.println(refreshToken.value());
        User user = userRepository.findByRefreshToken(refreshToken.value())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
        return jwtService.toTokenResponse(user.getEmail());
    }
}
