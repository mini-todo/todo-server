package com.example.todoproject.todo.domain;

import com.example.todoproject.todo.dto.TodoUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;

    private String content;
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private TodoType type;

    private Long userId;

    public Todo(String content, LocalDate date, TodoType type, Long userId) {
        this.content = content;
        this.date = date;
        this.type = type;
        this.userId = userId;
    }

    public void update(TodoUpdateRequest request, LocalDate localDate) {
        this.content = request.content();
        this.date = localDate;
        this.type = request.type();
    }

    public void updateType(TodoType type) {
        this.type = type;
    }
}
