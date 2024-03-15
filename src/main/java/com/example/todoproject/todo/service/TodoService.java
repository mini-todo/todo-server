package com.example.todoproject.todo.service;

import com.example.todoproject.common.time.Time;
import com.example.todoproject.todo.domain.FixedTodo;
import com.example.todoproject.todo.domain.Todo;
import com.example.todoproject.todo.domain.TodoType;
import com.example.todoproject.todo.dto.FixedTodoDto;
import com.example.todoproject.todo.dto.TodoCreateRequest;
import com.example.todoproject.todo.dto.TodoDailyResponse;
import com.example.todoproject.todo.dto.TodoListResponse;
import com.example.todoproject.todo.dto.TodoResponse;
import com.example.todoproject.todo.dto.TodoUpdateRequest;
import com.example.todoproject.todo.dto.TypeDto;
import com.example.todoproject.todo.repository.FixedTodoRepository;
import com.example.todoproject.todo.repository.TodoRepository;
import com.example.todoproject.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final FixedTodoRepository fixedTodoRepository;
    private final UserRepository userRepository;
    private final Time time;

    @Scheduled(cron = "0 1 0 * * *")
    public void addFixedTodo() {
        List<FixedTodo> allFixedTodo = fixedTodoRepository.findAll();
        for (FixedTodo fixedTodo : allFixedTodo) {
            Todo todo = new Todo(fixedTodo.getContent(), time.now(), TodoType.DAILY, fixedTodo.getUserId());
            todoRepository.save(todo);
        }
    }

    @Transactional
    public TodoResponse createTodo(TodoCreateRequest request, String email) {
        Todo todo = new Todo(request.content(), toDateInfo(request.date()), request.type(), getUserId(email));
        return new TodoResponse(todoRepository.save(todo).getId());
    }

    public TodoListResponse getTodoList(String email) {
        List<Todo> myTodo = todoRepository.findAllByUserId(getUserId(email));
        if (myTodo.isEmpty()) {
            return new TodoListResponse(new ArrayList<>());
        }

        List<TodoDailyResponse> myTodoList = myTodo.stream()
                .map(it -> new TodoDailyResponse(it.getId(), it.getContent(), it.getDate()))
                .toList();
        return new TodoListResponse(myTodoList);
    }

    public TodoDailyResponse getTodoDetail(String email, Long todoId) {
        Todo todo = todoRepository.findByUserIdAndId(getUserId(email), todoId)
                .orElseThrow(IllegalArgumentException::new);
        return new TodoDailyResponse(todo.getId(), todo.getContent(), todo.getDate());
    }

    @Transactional
    public void updateTodo(Long todoId, TodoUpdateRequest request) {
        Todo todo = getTodo(todoId);
        todo.update(request, toDateInfo(request.date()));
    }

    @Transactional
    public void updateType(Long todoId, TypeDto typeDto) {
        Todo todo = getTodo(todoId);
        todo.updateType(typeDto.type());
    }

    @Transactional
    public void deleteTodo(Long todoId) {
        todoRepository.deleteById(todoId);
    }

    @Transactional
    public void addFixedTodo(String email, FixedTodoDto dto) {
        FixedTodo fixedTodo = new FixedTodo(dto.content(), getUserId(email));
        fixedTodoRepository.save(fixedTodo);
    }

    @Transactional
    public void deleteFixedTodo(Long fixedTodoId) {
        fixedTodoRepository.deleteById(fixedTodoId);
    }

    private Long getUserId(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."))
                .getId();
    }

    private Todo getTodo(Long todoId) {
        return todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 할일을 찾을 수 없습니다."));
    }

    private LocalDate toDateInfo(String request) {
        List<Integer> dateInfo = Arrays.stream(request.split("-"))
                .map(Integer::parseInt)
                .toList();
        return LocalDate.of(dateInfo.get(0), dateInfo.get(1), dateInfo.get(2));
    }

}
