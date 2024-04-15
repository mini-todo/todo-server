package com.example.todoproject.todo.repository;

import com.example.todoproject.todo.domain.FixedTodo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FixedTodoRepository extends JpaRepository<FixedTodo, Long> {
    List<FixedTodo> findByUserId(Long userId);
}
