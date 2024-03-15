package com.example.todoproject.todo.dto;

import jakarta.validation.constraints.NotNull;

public record FixedTodoDto(
        @NotNull(message = "공백은 허용하지 않습니다.")
        String content
) {
}
