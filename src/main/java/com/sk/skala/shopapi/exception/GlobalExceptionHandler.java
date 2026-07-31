package com.sk.skala.shopapi.exception;

import com.sk.skala.shopapi.common.Response;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<Response<String>> handleResponseException(ResponseException e) {
        log.warn("ResponseException: {}", e.getMessage());
        return ResponseEntity
                .status(e.getError().getStatus())
                .body(new Response<>(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<String>> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse(Error.INVALID_PARAMETER.getMessage());

        log.warn("ValidationException: {}", message);
        return ResponseEntity
                .status(Error.INVALID_PARAMETER.getStatus())
                .body(new Response<>(message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<String>> handleException(Exception e) {
        log.error("Unhandled Exception", e);
        return ResponseEntity
                .status(Error.INTERNAL_SERVER_ERROR.getStatus())
                .body(new Response<>(Error.INTERNAL_SERVER_ERROR.getMessage()));
    }
}
