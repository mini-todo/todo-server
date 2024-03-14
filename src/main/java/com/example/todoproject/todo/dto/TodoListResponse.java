package com.example.todoproject.todo.dto;

import java.util.List;

public record TodoListResponse(
        List<TodoDailyResponse> todoList
) {
}
