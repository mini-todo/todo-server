package com.example.todoproject.error;

import com.example.todoproject.common.dto.CustomProblemDetail;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException ex
    ) {
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;
        CustomProblemDetail body = CustomProblemDetail.forStatusAndDetail(statusCode, ex.getMessage());

        return handleExceptionInternal(
                ex,
                body,
                new HttpHeaders(),
                statusCode
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(
            NotFoundException ex
    ) {
        HttpStatus statusCode = HttpStatus.NOT_FOUND;
        CustomProblemDetail body = CustomProblemDetail.forStatusAndDetail(statusCode, ex.getMessage());

        return handleExceptionInternal(
                ex,
                body,
                new HttpHeaders(),
                statusCode
        );
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNPException(
            NullPointerException ex
    ) {
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        CustomProblemDetail body = CustomProblemDetail.forStatusAndDetail(statusCode, ex.getMessage());

        return handleExceptionInternal(
                ex,
                body,
                new HttpHeaders(),
                statusCode
        );
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleUnclassifiedException(
            Exception ex
    ) {
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        CustomProblemDetail body = CustomProblemDetail.forStatusAndDetail(statusCode, ex.getMessage());

        return handleExceptionInternal(
                ex,
                body,
                new HttpHeaders(),
                statusCode
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodValidationException(MethodValidationException ex, HttpHeaders headers,
                                                                     HttpStatus status, WebRequest request) {
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;
        CustomProblemDetail body = CustomProblemDetail.forStatusAndDetail(statusCode, ex.getMessage());

        return handleExceptionInternal(
                ex,
                body,
                new HttpHeaders(),
                statusCode
        );
    }

    private ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers,
            HttpStatusCode statusCode) {
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
