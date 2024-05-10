package com.example.todoproject.util;

import com.example.todoproject.common.time.Time;
import java.time.LocalDate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class TestTime implements Time {

    @Override
    public LocalDate now() {
        return LocalDate.of(2024, 5, 16);
    }

    @TestConfiguration
    public static class TestConfig {

        @Bean
        @Primary
        public Time testTime() {
            return new TestTime();
        }

    }
}
