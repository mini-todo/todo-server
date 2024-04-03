package com.example.todoproject.todo.dto;

import com.example.todoproject.todo.domain.TodoType;
import java.time.LocalDate;

public record TodoDailyResponse(
        Long id,
        String title,
        String content,
        LocalDate date,
        boolean checked,
        boolean isFixed,
        TodoType type
) {
}
