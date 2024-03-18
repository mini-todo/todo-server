package com.example.todoproject.userservice;

import com.example.todoproject.common.jwt.service.JwtService;
import com.example.todoproject.redis.RefreshTokenRepository;
import com.example.todoproject.user.domain.User;
import com.example.todoproject.user.domain.UserRole;
import com.example.todoproject.user.dto.MyPageDto;
import com.example.todoproject.user.repository.UserRepository;
import com.example.todoproject.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired private UserRepository userRepository;
    @Autowired private RefreshTokenRepository refreshTokenRepository;
    @Autowired private JwtService jwtService;

    @Autowired private UserService userService;

    @Test
    void getMyPageTest() {
        User savedUSer = userRepository.save(new User(1L, "name", "email", UserRole.USER, " ", false));

        MyPageDto myPage = userService.getMyPage(savedUSer.getEmail());

        Assertions.assertThat(myPage.email()).isEqualTo(savedUSer.getEmail());
        Assertions.assertThat(myPage.name()).isEqualTo(savedUSer.getName());
    }

}
