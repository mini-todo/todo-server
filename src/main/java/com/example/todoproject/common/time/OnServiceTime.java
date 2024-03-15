package com.example.todoproject.common.time;

import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class OnServiceTime implements Time{
    @Override
    public LocalDate now() {
        return LocalDate.now();
    }
}
