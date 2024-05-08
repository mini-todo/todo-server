package com.example.todoproject.util;

import com.example.todoproject.common.time.Time;
import java.time.LocalDate;

public class TestTime implements Time {

    @Override
    public LocalDate now() {
        return LocalDate.of(2024, 5, 16);
    }
}
