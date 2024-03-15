package com.example.todoproject.todoservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.example.todoproject.todo.service.TodoService;
import com.example.todoproject.user.domain.User;
import com.example.todoproject.user.domain.UserRole;
import com.example.todoproject.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TodoServiceTest {

    @Autowired private FixedTodoRepository fixedTodoRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TodoRepository todoRepository;

    @Autowired
    private final TodoService todoService = new TodoService(
            todoRepository, fixedTodoRepository, userRepository
    );

    static String email = "test@example.com";

    @BeforeEach
    void beforeEach() {
        User user = new User(1L, "name", email, UserRole.USER, " ", "", false);
        userRepository.save(user);
    }

    @Test
    public void createTodoTest() {
        TodoCreateRequest request = new TodoCreateRequest("Test Todo", "2023-12-25", TodoType.DAILY);

        TodoResponse response = todoService.createTodo(request, email);

        assertNotNull(response.id());
        Optional<Todo> savedTodo = todoRepository.findById(response.id());
        assertTrue(savedTodo.isPresent());
    }

    @Test
    public void getTodoListTest() {
        // Given
        TodoCreateRequest request = new TodoCreateRequest("Test Todo", "2024-03-13", TodoType.DAILY);
        todoService.createTodo(request, email);

        // When
        TodoListResponse response = todoService.getTodoList(email);

        // Then
        assertNotNull(response);
        List<TodoDailyResponse> todoList = response.todoList();
        assertNotNull(todoList);
        assertFalse(todoList.isEmpty());
    }

    @Test
    public void getTodoDetailTest() {
        // Given
        String email = "test@example.com";
        TodoCreateRequest request = new TodoCreateRequest("Test Todo", "2024-03-13", TodoType.DAILY);
        TodoResponse createdTodo = todoService.createTodo(request, email);

        // When
        TodoDailyResponse response = todoService.getTodoDetail(email, createdTodo.id());

        // Then
        assertNotNull(response);
        assertEquals(createdTodo.id(), response.id());
        assertEquals(request.content(), response.content());
        assertEquals(LocalDate.parse("2024-03-13"), response.data());
    }

    @Test
    public void updateTodoTest() {
        // Given
        String email = "test@example.com";
        TodoCreateRequest createRequest = new TodoCreateRequest("Test Todo", "2024-03-13", TodoType.DAILY);
        TodoResponse createdTodo = todoService.createTodo(createRequest, email);
        TodoUpdateRequest updateRequest = new TodoUpdateRequest("Updated Todo", "2024-03-14", TodoType.DAILY);

        // When
        todoService.updateTodo(createdTodo.id(), updateRequest);
        TodoDailyResponse updatedTodo = todoService.getTodoDetail(email, createdTodo.id());

        // Then
        assertNotNull(updatedTodo);
        assertEquals(updateRequest.content(), updatedTodo.content());
        assertEquals(LocalDate.parse("2024-03-14"), updatedTodo.data());
    }

    @Test
    public void updateTypeTest() {
        // Given
        String email = "test@example.com";
        TodoCreateRequest createRequest = new TodoCreateRequest("Test Todo", "2024-03-13", TodoType.DAILY);
        TodoResponse createdTodo = todoService.createTodo(createRequest, email);
        TypeDto typeDto = new TypeDto(TodoType.WEEKLY);

        // When
        todoService.updateType(createdTodo.id(), typeDto);
        TodoDailyResponse updatedTodo = todoService.getTodoDetail(email, createdTodo.id());

        // Then
        assertNotNull(updatedTodo);
    }

    @Test
    public void deleteTodoTest() {
        // Given
        String email = "test@example.com";
        TodoCreateRequest request = new TodoCreateRequest("Test Todo", "2024-03-13", TodoType.DAILY);
        TodoResponse createdTodo = todoService.createTodo(request, email);

        // When
        todoService.deleteTodo(createdTodo.id());

        // Then
        assertThrows(IllegalArgumentException.class, () -> todoService.getTodoDetail(email, createdTodo.id()));
    }

    @Test
    public void addFixedTodoTest() {
        // Given
        String email = "test@example.com";
        FixedTodoDto fixedTodoDto = new FixedTodoDto("Fixed Todo Content");

        // When
        todoService.addFixedTodo(email, fixedTodoDto);

        // Then
        List<FixedTodo> fixedTodos = fixedTodoRepository.findAll();
        assertEquals(1, fixedTodos.size());
        assertEquals(fixedTodoDto.content(), fixedTodos.get(0).getContent());
    }


}
