package com.example.tasknewspring.exception;

import lombok.Getter;

@Getter
public class IllegalStateException extends RuntimeException {
    private final ErrorCodes errorCode;

    public IllegalStateException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
