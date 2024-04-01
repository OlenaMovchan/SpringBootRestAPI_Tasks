package com.example.tasknewspring.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {
    private final ErrorCodes errorCode;
    public UserAlreadyExistsException(String message, ErrorCodes errorCode) {

        super(message);
        this.errorCode = errorCode;
    }

}
