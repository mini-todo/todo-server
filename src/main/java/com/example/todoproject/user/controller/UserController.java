package com.example.todoproject.user.controller;

import com.example.todoproject.common.dto.CommonResponse;
import com.example.todoproject.common.dto.EmptyDto;
import com.example.todoproject.common.dto.TokenResponse;
import com.example.todoproject.redis.RefreshTokenRepository;
import com.example.todoproject.todo.dto.RefreshTokenDto;
import com.example.todoproject.user.dto.MyPageDto;
import com.example.todoproject.user.dto.RedisDto;
import com.example.todoproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/login")
    public CommonResponse<TokenResponse> issueToken(@RequestParam(value = "token") String token) {
        TokenResponse tokenResponse = userService.issueToken(token);
        return new CommonResponse<>(tokenResponse);
    }

    @PostMapping("/refreshToken")
    public CommonResponse<TokenResponse> reIssueToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        TokenResponse tokenResponse = userService.reIssueToken(refreshTokenDto);
        return new CommonResponse<>(tokenResponse);
    }

    @GetMapping("/me")
    public CommonResponse<MyPageDto> getMyPage() {
        return new CommonResponse<>(userService.getMyPage(getUserName()));
    }

    private String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping("/save/token")
    public CommonResponse<EmptyDto> saveToken(@RequestBody RedisDto redisDto) {
        userService.issue(redisDto);
        return new CommonResponse<>(new EmptyDto());
    }
}
