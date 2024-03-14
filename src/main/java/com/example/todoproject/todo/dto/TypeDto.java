package com.example.todoproject.todo.dto;

import com.example.todoproject.todo.domain.TodoType;
import jakarta.validation.constraints.NotNull;

public record TypeDto(
        @NotNull
        TodoType type
) {
}
