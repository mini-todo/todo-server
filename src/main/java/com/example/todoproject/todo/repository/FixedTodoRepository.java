package com.example.todoproject.todo.repository;

import com.example.todoproject.todo.domain.FixedTodo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FixedTodoRepository extends JpaRepository<FixedTodo, Long> {
}
