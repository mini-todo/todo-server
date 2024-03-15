package com.example.todoproject.todo.dto;

import com.example.todoproject.todo.domain.TodoType;
import jakarta.validation.constraints.NotNull;

public record TodoUpdateRequest(
        @NotNull(message = "공백은 허용하지 않습니다.")
        String content,
        @NotNull(message = "공백은 허용하지 않습니다.")
        String date,
        @NotNull(message = "공백은 허용하지 않습니다.")
        TodoType type
) {
}
