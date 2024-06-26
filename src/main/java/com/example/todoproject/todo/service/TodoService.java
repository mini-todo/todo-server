package com.example.todoproject.todo.service;

import com.example.todoproject.common.time.Time;
import com.example.todoproject.todo.domain.FixedTodo;
import com.example.todoproject.todo.domain.Todo;
import com.example.todoproject.todo.domain.TodoType;
import com.example.todoproject.todo.dto.TodoCreateRequest;
import com.example.todoproject.todo.dto.TodoDailyResponse;
import com.example.todoproject.todo.dto.TodoListResponse;
import com.example.todoproject.todo.dto.TodoResponse;
import com.example.todoproject.todo.dto.TodoUpdateRequest;
import com.example.todoproject.todo.dto.TypeDto;
import com.example.todoproject.todo.repository.FixedTodoRepository;
import com.example.todoproject.todo.repository.TodoRepository;
import com.example.todoproject.user.repository.UserRepository;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final FixedTodoRepository fixedTodoRepository;
    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;
    private final Time time;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void addFixedTodoWithJDBC_SQL() {
        List<FixedTodo> allFixedTodo = fixedTodoRepository.findAll();
        Date today = Date.valueOf(time.now());
        String sql = "insert into todo (title, content, date, checked, is_fixed, user_email, type, user_id) values (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, allFixedTodo.get(i).getTitle());
                ps.setString(2, allFixedTodo.get(i).getContent());
                ps.setDate(3, today);
                ps.setBoolean(4, false);
                ps.setBoolean(5, true);
                ps.setString(6, allFixedTodo.get(i).getUserEmail());
                ps.setString(7, TodoType.DAILY.toString());
                ps.setLong(8, allFixedTodo.get(i).getUserId());

            }

            @Override
            public int getBatchSize() {
                return allFixedTodo.size();
            }
        });
    }

    @Transactional
    public TodoResponse createTodo(TodoCreateRequest request, String email) {
        Long userId = getUserId(email);
        if (request.isFixed()) {
            FixedTodo fixedTodo = new FixedTodo(request.title(), request.content(), getUserId(email), email);
            fixedTodoRepository.save(fixedTodo);
            return saveTodo(request, time.now(), email, userId, true);
        }
        return saveTodo(request, toDateInfo(request.date()), email, userId, false);
    }

    private TodoResponse saveTodo(TodoCreateRequest request, LocalDate time, String email, Long userId, boolean isFixed) {
        Todo todo = new Todo(
                request.title(),
                request.content(),
                time,
                email,
                request.type(),
                userId,
                isFixed);
        return new TodoResponse(todoRepository.save(todo).getId());
    }

    public TodoListResponse getTodoList(String email, TodoType type) {
        List<Todo> myTodo;
        System.out.println(time.now().toString());
        if (type ==TodoType.DAILY) {
            myTodo = todoRepository.findAllDailyByUserIdAndAndDate(getUserId(email), time.now(), type);
        } else {
            myTodo = todoRepository.findAllMonthlyByUserIdAndAndDate(getUserId(email), time.now(), type);
        }

        if (myTodo.isEmpty()) {
            return new TodoListResponse(new ArrayList<>());
        }

        List<TodoDailyResponse> myTodoList = myTodo.stream()
                .map(it -> new TodoDailyResponse(it.getId(),it.getTitle(), it.getContent(), it.getDate(), it.isChecked(), it.isFixed(), it.getType()))
                .toList();
        return new TodoListResponse(myTodoList);
    }

    public TodoListResponse getFixedTodoList(String email) {
        List<FixedTodo> myFixedTodo = fixedTodoRepository.findByUserId(getUserId(email));
        if (myFixedTodo.isEmpty()) {
            return new TodoListResponse(new ArrayList<>());
        }

        List<TodoDailyResponse> myTodoList = myFixedTodo.stream()
                .map(it -> new TodoDailyResponse(it.getId(),it.getTitle(), it.getContent(), time.now(), false, true, TodoType.DAILY))
                .toList();
        return new TodoListResponse(myTodoList);
    }

    public TodoDailyResponse getTodoDetail(String email, Long todoId) {
        Todo todo = todoRepository.findByUserIdAndId(getUserId(email), todoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 할일을 찾을 수 없습니다."));
        return new TodoDailyResponse(todo.getId(),todo.getTitle(), todo.getContent(), todo.getDate(), todo.isChecked(), todo.isFixed(), todo.getType());
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
    public void updateFixedTodo(Long fixedTodoId, TodoUpdateRequest updateRequest) {
        FixedTodo findFixedTodo = getFixedTodo(fixedTodoId);
        findFixedTodo.update(updateRequest);
    }

    @Transactional
    public void checkTodo(Long todoId, String userName) {
        Todo todo = getTodo(todoId);
        if (!todo.getUserEmail().equals(userName)) {
            throw new IllegalArgumentException("본인이 아닙니다.");
        }
        todo.check();
    }

    @Transactional
    public void deleteTodo(Long todoId) {
        todoRepository.deleteById(todoId);
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

    private FixedTodo getFixedTodo(Long fixedTodoId) {
        return fixedTodoRepository.findById(fixedTodoId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
