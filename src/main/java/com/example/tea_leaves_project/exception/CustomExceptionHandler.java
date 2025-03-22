package com.example.tea_leaves_project.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleApiException(ApiException err, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                err.getCode().code,
                err.getCode().status,
                err.getCode().message
        );
        return ResponseEntity.status(err.getCode().code).body(errorResponse);
    }
}
