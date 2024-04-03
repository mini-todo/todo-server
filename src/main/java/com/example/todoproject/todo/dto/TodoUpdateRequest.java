package com.example.todoproject.todo.dto;

import com.example.todoproject.todo.domain.TodoType;
import jakarta.validation.constraints.NotNull;

public record TodoUpdateRequest(
        @NotNull
        String title,
        @NotNull
        String content,
        @NotNull
        String date,
        @NotNull
        TodoType type
) {
}
