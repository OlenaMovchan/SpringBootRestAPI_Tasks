package com.example.tasknewspring.exception;

import lombok.Getter;

@Getter
public enum ErrorCodes {
    USER_NOT_FOUND(1000),
    USER_ALREADY_EXISTS(1001),
    TASK_NOT_FOUND(1002),
    TASK_NOT_VALID(1003),
    INCORRECT_TASK_STATE(1004),
    ILLEGAL_ARGUMENT(1005),
    UNAUTHORIZED(1006),
    ACCESS_DENIED(1007),
    INTERNAL_SERVER_ERROR(1008),
    BAD_REQUEST(1009);

    private final int code;

    ErrorCodes(int code) {
        this.code = code;
    }
}
