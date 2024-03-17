package com.example.todoproject.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "jwtToken", timeToLive = 60*60*24*14)
public class RefreshToken {

    @Id
    private Long userId;

    @Indexed
    private String token;
}
