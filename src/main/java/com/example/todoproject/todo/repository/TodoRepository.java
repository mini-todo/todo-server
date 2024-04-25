package com.example.todoproject.todo.repository;

import com.example.todoproject.todo.domain.Todo;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    Optional<Todo> findByUserIdAndId(Long userId, Long id);

    @Query("select t from Todo t where t.userId = :userId and MONTH(:date) = MONTH(t.date)")
    List<Todo> findAllMonthlyByUserIdAndAndDate(@Param("userId") Long userId,@Param("date") LocalDate date);

    @Query("select t from Todo t where t.userId = :userId and DATE(:date) = DATE(t.date)")
    List<Todo> findAllDailyByUserIdAndAndDate(@Param("userId") Long userId,@Param("date") LocalDate date);
}
