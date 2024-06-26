package com.example.todoproject.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USERS")
@Builder
@AllArgsConstructor
@Getter
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name;
    private String email;
    private String profile;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private String socialId;

    @Column(name = "is_deleted")
    private boolean deleted;

    public User(String name, String email, String profile, UserRole userRole, String socialId, boolean deleted) {
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.userRole = userRole;
        this.socialId = socialId;
        this.deleted = deleted;
    }
}
