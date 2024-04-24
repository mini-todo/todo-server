package com.example.todoproject.aop.logtrace;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TraceStatus {
    private TraceId traceId;
    private Long startTimesMs;
    private String message;
}
