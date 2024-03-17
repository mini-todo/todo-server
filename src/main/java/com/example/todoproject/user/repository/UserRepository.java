package com.example.todoproject.user.repository;

import com.example.todoproject.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findBySocialId(String socialId);

    @Query("select u from User u where u.refreshToken = :refreshToken")
    Optional<User> findByRefreshToken(@Param("refreshToken") String refreshToken);
}
