package com.example.todoproject.todoservice;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.todoproject.todo.domain.FixedTodo;
import com.example.todoproject.todo.dto.FixedTodoDto;
import com.example.todoproject.todo.dto.TodoDailyResponse;
import com.example.todoproject.todo.repository.FixedTodoRepository;
import com.example.todoproject.todo.repository.TodoRepository;
import com.example.todoproject.todo.service.TodoService;
import com.example.todoproject.user.domain.User;
import com.example.todoproject.user.domain.UserRole;
import com.example.todoproject.user.repository.UserRepository;
import com.example.todoproject.util.TestTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class FixedTodoTest {

    @Autowired
    private FixedTodoRepository fixedTodoRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TodoRepository todoRepository;

    @Autowired
    private final TodoService todoService = new TodoService(
            todoRepository, fixedTodoRepository, userRepository, new TestTime()
    );

    static String email = "test@example.com";

    @BeforeEach
    void beforeEach() {
        User user = new User(1L, "name", email, UserRole.USER, " ", "", false);
        userRepository.save(user);
    }

    @Test
    public void createFixedTodoTest() {
        // Given
        String email = "test@example.com";
        FixedTodoDto fixedTodoDto = new FixedTodoDto("Fixed Todo Content");

        // When
        todoService.createFixedTodo(email, fixedTodoDto);

        // Then
        List<FixedTodo> fixedTodos = fixedTodoRepository.findAll();
        assertEquals(1, fixedTodos.size());
        assertEquals(fixedTodoDto.content(), fixedTodos.get(0).getContent());
    }

    @Test
    public void addFixedTodo() {
        // Given
        String email = "test@example.com";
        FixedTodoDto fixedTodoDto = new FixedTodoDto("Fixed Todo Content");
        todoService.createFixedTodo(email, fixedTodoDto);

        // When
        todoService.addFixedTodo();

        // Then
        List<TodoDailyResponse> list = todoService.getTodoList(email).todoList();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).content()).isEqualTo("Fixed Todo Content");
    }

}
