package com.example.todoproject.todo.controller;

import com.example.todoproject.common.dto.CommonResponse;
import com.example.todoproject.common.dto.EmptyDto;
import com.example.todoproject.todo.dto.FixedTodoDto;
import com.example.todoproject.todo.dto.TodoCreateRequest;
import com.example.todoproject.todo.dto.TodoDailyResponse;
import com.example.todoproject.todo.dto.TodoListResponse;
import com.example.todoproject.todo.dto.TodoResponse;
import com.example.todoproject.todo.dto.TodoUpdateRequest;
import com.example.todoproject.todo.dto.TypeDto;
import com.example.todoproject.todo.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public CommonResponse<TodoResponse> createTodo(@Valid @RequestBody TodoCreateRequest todoCreateRequest) {
        return new CommonResponse<>(todoService.createTodo(todoCreateRequest, getUserName()));
    }

    @GetMapping("/daily")
    public CommonResponse<TodoListResponse> getDailyTodoList() {
        return new CommonResponse<>(todoService.getTodoList(getUserName()));
    }

    @GetMapping("/monthly")
    public CommonResponse<TodoListResponse> getMonthlyTodoList() {
        return new CommonResponse<>(todoService.getTodoList(getUserName()));
    }

    @GetMapping("/{todoId}")
    public CommonResponse<TodoDailyResponse> getTodoDetail(@PathVariable("todoId") Long todoId) {
        return new CommonResponse<>(todoService.getTodoDetail(getUserName(), todoId));
    }

    @PatchMapping("/{todoId}")
    public CommonResponse<EmptyDto> updateTodo(@PathVariable("todoId") Long todoId, @Valid @RequestBody TodoUpdateRequest todoCreateRequest) {
        todoService.updateTodo(todoId, todoCreateRequest);
        return new CommonResponse<>(new EmptyDto());
    }

    @PatchMapping("/drag/{todoId}")
    public CommonResponse<EmptyDto> updateTodoType(@PathVariable("todoId") Long todoId, @Valid @RequestBody TypeDto typeDto) {
        todoService.updateType(todoId, typeDto);
        return new CommonResponse<>(new EmptyDto());
    }

    @DeleteMapping("/{todoId}")
    public CommonResponse<EmptyDto> deleteTodo(@PathVariable("todoId") Long todoId) {
        todoService.deleteTodo(todoId);
        return new CommonResponse<>(new EmptyDto());
    }

    @PostMapping("/fixed")
    public CommonResponse<EmptyDto> createFixedTodo(@Valid @RequestBody FixedTodoDto dto) {
        todoService.addFixedTodo(getUserName(), dto);
        return new CommonResponse<>(new EmptyDto());
    }

    @DeleteMapping("/fixed/{fixedTodoId}")
    public CommonResponse<EmptyDto> deleteFixedTodo(@PathVariable("fixedTodoId") Long fixedTodoId) {
        todoService.deleteFixedTodo(fixedTodoId);
        return new CommonResponse<>(new EmptyDto());
    }

    private String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
