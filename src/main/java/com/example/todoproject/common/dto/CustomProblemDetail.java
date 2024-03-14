package com.example.todoproject.common.dto;

import org.springframework.http.HttpStatus;

public record CustomProblemDetail(
        String title,
        int status,
        String detail
) {

    public static CustomProblemDetail forStatusAndDetail(
            HttpStatus status,
            String detail
    ) {
        return new CustomProblemDetail(
                status.getReasonPhrase(),
                status.value(),
                detail
        );
    }

}
