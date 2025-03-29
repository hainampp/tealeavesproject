package com.example.tea_leaves_project.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

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
