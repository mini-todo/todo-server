package com.example.todoproject.common.login;

import com.example.todoproject.user.domain.User;
import com.example.todoproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService{

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);

        return org.springframework.security.core.userdetails.User.builder()
                .username(email)
                .password("password")
                .roles(user.getUserRole().name())
                .build();
    }
}
