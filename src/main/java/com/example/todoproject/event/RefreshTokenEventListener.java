package com.example.todoproject.event;

import com.example.todoproject.redis.RefreshToken;
import com.example.todoproject.redis.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenEventListener {

    private final RefreshTokenRepository refreshTokenRepository;

    @Async
    @EventListener
    public void saveRefreshToken(RefreshTokenEvent event) {
        refreshTokenRepository.save(new RefreshToken(event.email(), event.refreshToken()));
    }

}
