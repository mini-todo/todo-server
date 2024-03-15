package com.example.todoproject.todo.dto;

import com.example.todoproject.todo.domain.TodoType;
import jakarta.validation.constraints.NotNull;

public record TypeDto(
        @NotNull(message = "공백은 허용하지 않습니다.")
        TodoType type
) {
}
