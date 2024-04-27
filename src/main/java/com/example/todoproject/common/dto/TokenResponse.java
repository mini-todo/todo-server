package com.example.todoproject.common.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String name
) {
}
