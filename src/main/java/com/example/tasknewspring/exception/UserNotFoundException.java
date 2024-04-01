package com.example.tasknewspring.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final ErrorCodes errorCode;

    public UserNotFoundException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
