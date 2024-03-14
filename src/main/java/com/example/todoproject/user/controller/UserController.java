package com.example.todoproject.user.controller;

import com.example.todoproject.common.dto.CommonResponse;
import com.example.todoproject.common.dto.TokenResponse;
import com.example.todoproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
}
