package com.example.todoproject.user.dto;

public record LoginRequest(
        String name,
        String email,
        String providerId,
        String profile
) {
}
