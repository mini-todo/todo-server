package com.example.todoproject.user.domain;

//import com.example.todoproject.team.domain.Team;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private String socialId;

    @Column(name = "is_deleted")
    private boolean deleted;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Team team;
//
//    public void addTeam(Team team) {
//        this.team = team;
//    }
//
//    public void exit(Team team) {
//
//    }
}
