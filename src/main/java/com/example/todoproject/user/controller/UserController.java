package com.example.todoproject.user.controller;

import com.example.todoproject.common.dto.CommonResponse;
import com.example.todoproject.common.dto.TokenResponse;
import com.example.todoproject.redis.RefreshTokenService;
import com.example.todoproject.todo.dto.RefreshTokenDto;
import com.example.todoproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/login")
    public CommonResponse<TokenResponse> issueToken(@RequestParam(value = "token") String token) {
        TokenResponse tokenResponse = userService.issueToken(token);
        return new CommonResponse<>(tokenResponse);
    }

    @PostMapping("/refreshToken")
    public CommonResponse<TokenResponse> reIssueToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        TokenResponse tokenResponse = userService.reIssueToken(refreshTokenDto);
        refreshTokenService.removeRefreshToken(refreshTokenDto.value());
        return new CommonResponse<>(tokenResponse);
    }
}
