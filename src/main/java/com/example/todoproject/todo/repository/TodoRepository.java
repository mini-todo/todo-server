package com.example.todoproject.todo.repository;

import com.example.todoproject.todo.domain.Todo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findAllByUserId(Long userId);
    Optional<Todo> findByUserIdAndId(Long userId, Long id);
}
