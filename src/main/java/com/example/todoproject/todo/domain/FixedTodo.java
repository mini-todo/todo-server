package com.example.todoproject.todo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FixedTodo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fixed_todo_id")
    private Long id;

    private String content;
    private Long userId;

    public FixedTodo(String content, Long userId) {
        this.content = content;
        this.userId = userId;
    }
}
