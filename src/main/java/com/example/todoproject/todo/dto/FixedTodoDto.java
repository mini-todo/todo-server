package com.example.todoproject.todo.dto;

import jakarta.validation.constraints.NotNull;

public record FixedTodoDto(
        @NotNull
        String content
) {
}
