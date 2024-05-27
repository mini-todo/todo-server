package com.example.todoproject.todoservice;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.todoproject.common.time.Time;
import com.example.todoproject.todo.domain.FixedTodo;
import com.example.todoproject.todo.domain.Todo;
import com.example.todoproject.todo.domain.TodoType;
import com.example.todoproject.todo.dto.TodoCreateRequest;
import com.example.todoproject.todo.dto.TodoDailyResponse;
import com.example.todoproject.todo.dto.TodoListResponse;
import com.example.todoproject.todo.dto.TodoResponse;
import com.example.todoproject.todo.dto.TodoUpdateRequest;
import com.example.todoproject.todo.repository.FixedTodoRepository;
import com.example.todoproject.todo.repository.TodoRepository;
import com.example.todoproject.todo.service.TodoService;
import com.example.todoproject.user.domain.User;
import com.example.todoproject.user.domain.UserRole;
import com.example.todoproject.user.repository.UserRepository;
import com.example.todoproject.util.TestTime.TestConfig;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Import({TestConfig.class})
public class TodoServiceTest {

    @Autowired FixedTodoRepository fixedTodoRepository;
    @Autowired UserRepository userRepository;
    @Autowired TodoRepository todoRepository;
    @Autowired JdbcTemplate jdbcTemplate;
    @Autowired Time time;
    @Autowired TodoService todoService;

    static String email = "test@example.com";

    @BeforeEach
    void beforeEach() {
        User user = new User(1L, "name", email,"profile", UserRole.USER, " ", false);
        userRepository.save(user);
    }

    @Test
    @DisplayName("고정 투두를 오늘의 투두에 저장")
    void addFixedTodoToTodo() {
        //given
        FixedTodo fixedTodo = new FixedTodo("title", "content", 1L, email);
        FixedTodo fixedTodo2 = new FixedTodo("title", "content", 1L, email);
        FixedTodo fixedTodo3 = new FixedTodo("title", "content", 1L, email);
        fixedTodoRepository.saveAll(List.of(fixedTodo3, fixedTodo2, fixedTodo));

        //when
        todoService.addFixedTodoWithJDBC_SQL();

        //then
        List<Todo> allTodo = todoRepository.findAll();
        assertThat(allTodo.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("일반 투두 생성 테스트")
    public void createNormalTodoTest() {
        //given
        TodoCreateRequest request = new TodoCreateRequest("title", "Test Todo", "2024-03-16", TodoType.DAILY, false);

        //when
        TodoResponse response = todoService.createTodo(request, email);

        //then
        assertNotNull(response.id());
        Optional<Todo> savedTodo = todoRepository.findById(response.id());
        assertTrue(savedTodo.isPresent());
    }

    @Test
    @DisplayName("고정 투두 생성 테스트")
    void createFixedTodoTest() {
        //given
        TodoCreateRequest request = new TodoCreateRequest("title", "Test Todo", "2024-03-16", TodoType.DAILY, true);

        //when
        todoService.createTodo(request, email);

        //then
        List<FixedTodo> all = fixedTodoRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("일간 투두 조회 테스트")
    void findAllDailyTodo() {
        //given
        TodoCreateRequest request = new TodoCreateRequest("title", "Test Todo", "2024-05-16", TodoType.DAILY, false);
        todoService.createTodo(request, email);
        todoService.createTodo(request, email);
        todoService.createTodo(request, email);

        //when
        TodoListResponse todoList = todoService.getTodoList(email, TodoType.DAILY);

        //then
        assertThat(todoList.todoList().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("월간 조회 테스트")
    void findAllMonthlyTodo() {
        //given
        TodoCreateRequest request = new TodoCreateRequest("title", "Test Todo", "2024-05-16", TodoType.MONTHLY, false);
        todoService.createTodo(request, email);
        todoService.createTodo(request, email);
        todoService.createTodo(request, email);

        //when
        TodoListResponse todoList = todoService.getTodoList(email, TodoType.MONTHLY);

        //then
        assertThat(todoList.todoList().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("투두 없는 조회 테스트")
    void findAllNoTodo() {
        //given

        //when
        TodoListResponse todoList = todoService.getTodoList(email, TodoType.DAILY);

        //then
        assertThat(todoList.todoList().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("고정 투두 조회 테스트")
    void findAllFixedTodoTest() {
        //given
        TodoCreateRequest request = new TodoCreateRequest("title", "Test Todo", "2023-12-25", TodoType.DAILY, true);
        TodoResponse response = todoService.createTodo(request, email);
        TodoResponse response2 = todoService.createTodo(request, email);
        TodoResponse response3 = todoService.createTodo(request, email);

        //when
        TodoListResponse fixedTodoList = todoService.getFixedTodoList(email);
        List<TodoDailyResponse> todoDailyResponses = fixedTodoList.todoList();

        //then
        assertThat(todoDailyResponses.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("투두 상세 조회 테스트")
    public void getTodoDetailTest() {
        // Given
        TodoCreateRequest request = new TodoCreateRequest("title", "Test Todo", "2024-03-13", TodoType.DAILY, false);
        TodoResponse createdTodo = todoService.createTodo(request, email);

        // When
        TodoDailyResponse response = todoService.getTodoDetail(email, createdTodo.id());

        // Then
        assertNotNull(response);
        assertEquals(createdTodo.id(), response.id());
        assertEquals(request.content(), response.content());
        assertEquals(LocalDate.parse("2024-03-13"), response.date());
    }

    @Test
    @DisplayName("투두 상세 조회 실패 테스트")
    public void getTodoDetailFailTest() {
        // Given
        TodoCreateRequest request = new TodoCreateRequest("title", "Test Todo", "2023-12-25", TodoType.DAILY, false);
        todoService.createTodo(request, email);

        // When & Then
        assertThatThrownBy(() -> todoService.getTodoDetail(email, 0L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 할일을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("투두 수정 테스트")
    public void updateTodoTest() {
        // Given
        TodoCreateRequest request = new TodoCreateRequest("title", "Test Todo", "2023-12-25", TodoType.DAILY, false);
        TodoResponse createdTodo = todoService.createTodo(request, email);
        TodoUpdateRequest updateRequest = new TodoUpdateRequest("title", "update Todo", "2024-03-14", TodoType.DAILY);

        // When
        todoService.updateTodo(createdTodo.id(), updateRequest);
        TodoDailyResponse updatedTodo = todoService.getTodoDetail(email, createdTodo.id());

        // Then
        assertNotNull(updatedTodo);
        assertEquals(updateRequest.content(), updatedTodo.content());
        assertEquals(LocalDate.parse("2024-03-14"), updatedTodo.date());
    }

    @Test
    @DisplayName("투두 수정 실패 테스트")
    public void updateTodoDetailFailTest() {
        // Given
        TodoCreateRequest request = new TodoCreateRequest("title", "Test Todo", "2023-12-25", TodoType.DAILY, false);
        todoService.createTodo(request, email);
        TodoUpdateRequest updateRequest = new TodoUpdateRequest("title", "update Todo", "2024-03-14", TodoType.DAILY);

        // When & Then
        assertThatThrownBy(() -> todoService.updateTodo(100L, updateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 할일을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("투두 체크 테스트")
    void checkTodoTest() {
        //given
        TodoCreateRequest request = new TodoCreateRequest("title", "Test Todo", "2023-12-25", TodoType.DAILY, false);
        TodoResponse todo = todoService.createTodo(request, email);

        //when
        todoService.checkTodo(todo.id(), email);

        //then
        TodoDailyResponse todoDetail = todoService.getTodoDetail(email, todo.id());
        assertThat(todoDetail.checked()).isTrue();
    }

    @Test
    public void deleteTodoTest() {
        // Given
        String email = "test@example.com";
        TodoCreateRequest request = new TodoCreateRequest("title", "Test Todo", "2023-12-25", TodoType.DAILY, false);
        TodoResponse createdTodo = todoService.createTodo(request, email);

        // When
        todoService.deleteTodo(createdTodo.id());

        // Then
        assertThrows(IllegalArgumentException.class, () -> todoService.getTodoDetail(email, createdTodo.id()));
    }


}
