package com.example.tasknewspring.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class InvalidTaskException extends RuntimeException{
    private ErrorCodes errorCode;
    private List<String> errors;

    public InvalidTaskException(String message) {
        super(message);
    }

    public InvalidTaskException(String message, Throwable cause){

        super(message, cause);
    }

    public InvalidTaskException(String message, Throwable cause, ErrorCodes errorCode){
        super(message, cause);
        this.errorCode = errorCode;
    }

    public InvalidTaskException(String message, Throwable cause, ErrorCodes errorCode, List<String> errors){
        super(message, cause);
        this.errorCode = errorCode;
        this.errors = errors;
    }

    public InvalidTaskException(String message, ErrorCodes errorCode){
        super(message);
        this.errorCode = errorCode;

    }

    public InvalidTaskException(String message, ErrorCodes errorCode, List<String> errors){
        super(message);
        this.errorCode = errorCode;
        this.errors = errors;
    }

}