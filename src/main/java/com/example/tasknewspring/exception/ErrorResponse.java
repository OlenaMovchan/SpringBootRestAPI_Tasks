package com.example.tasknewspring.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int statusCode;
    private int errorCode;
    private String message;
    List<String> errors = new ArrayList<>();
}
