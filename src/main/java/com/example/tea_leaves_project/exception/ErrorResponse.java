package com.example.tea_leaves_project.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private int code;
    private String status;
    private String message;

    ErrorResponse(int code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}
