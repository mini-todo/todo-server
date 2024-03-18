package com.example.todoproject.event;

public record RefreshTokenEvent(
        String email,
        String refreshToken
) {
}
