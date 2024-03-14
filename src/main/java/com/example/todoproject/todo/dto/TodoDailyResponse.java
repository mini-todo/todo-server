package com.example.todoproject.todo.dto;

import java.time.LocalDate;

public record TodoDailyResponse(
        Long id,
        String content,
        LocalDate data
) {
}
