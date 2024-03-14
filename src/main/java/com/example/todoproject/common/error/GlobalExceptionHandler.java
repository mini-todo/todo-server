package com.example.todoproject.common.error;

import com.example.todoproject.common.dto.CustomProblemDetail;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest request
    ) {
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;
        CustomProblemDetail body = CustomProblemDetail.forStatusAndDetail(statusCode, ex.getMessage());

        return handleExceptionInternal(
                ex,
                body,
                new HttpHeaders(),
                statusCode,
                request
        );
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleUnclassifiedException(
            Exception ex,
            WebRequest request
    ) {
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        CustomProblemDetail body = CustomProblemDetail.forStatusAndDetail(statusCode, ex.getMessage());

        return handleExceptionInternal(
                ex,
                body,
                new HttpHeaders(),
                statusCode,
                request
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;
        CustomProblemDetail body = CustomProblemDetail.forStatusAndDetail(statusCode, ex.getMessage());

        return handleExceptionInternal(
                ex,
                body,
                new HttpHeaders(),
                statusCode,
                request
        );
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers,
            HttpStatusCode statusCode, WebRequest request) {
        if (statusCode.is5xxServerError()) {
            log.error(ex.getMessage());
        }

        Map<String, Object> errorBody = Map.of("error", body);

        return new ResponseEntity<>(
                errorBody,
                headers,
                statusCode
        );
    }
}
