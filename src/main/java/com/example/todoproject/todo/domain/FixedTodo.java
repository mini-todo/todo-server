package com.example.todoproject.todo.domain;

import com.example.todoproject.todo.dto.TodoUpdateRequest;
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

    private String title;
    private String content;
    private Long userId;

    public FixedTodo(String title, String content, Long userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

    public void update(TodoUpdateRequest updateRequest) {
        this.title = updateRequest.title();
        this.content = updateRequest.content();
    }
}
