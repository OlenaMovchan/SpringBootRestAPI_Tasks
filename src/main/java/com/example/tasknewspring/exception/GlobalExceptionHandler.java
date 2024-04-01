package com.example.tasknewspring.exception;

import com.example.tasknewspring.util.LocaleMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;
import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler{ //extends ResponseEntityExceptionHandler {

    private final LocaleMessage localeMessage;

    @ExceptionHandler(value = {TaskNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleTaskNotFoundException(TaskNotFoundException ex, WebRequest request) {
        log.error("Error, task not found", ex);
        Locale locale = getLocaleFromHeader(request);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .errorCode(ex.getErrorCode().getCode())
                .message(localeMessage.getLocaleMessage("exception.task.notfound", locale))
                .build();
        request.getDescription(false);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InvalidTaskException.class})
    public ResponseEntity<ErrorResponse> handleInvalidTaskException(InvalidTaskException ex, WebRequest request) {
        log.error("Error, task not valid", ex);
        Locale locale = getLocaleFromHeader(request);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errorCode(ex.getErrorCode().getCode())
                .message(localeMessage.getLocaleMessage("exception.task.notvalid", locale))
                .errors(ex.getErrors())
                .build();
        request.getDescription(false);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = {IllegalStateException.class})
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        log.error("Error, illegal state", ex);
        Locale locale = getLocaleFromHeader(request);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errorCode(ex.getErrorCode().getCode())
                .message(localeMessage.getLocaleMessage("exception.task.illegalstate", locale))
                .build();
        request.getDescription(false);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        log.error("Error, access denied", ex);
        Locale locale = getLocaleFromHeader(request);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.FORBIDDEN.value())
                .errorCode(ErrorCodes.ACCESS_DENIED.getCode())
                .message(localeMessage.getLocaleMessage("exception.accessdenied", locale))
                .build();
        request.getDescription(false);

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {UserAlreadyExistsException.class})
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
        log.error("Error, user already exists", ex);
        Locale locale = getLocaleFromHeader(request);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errorCode(ErrorCodes.USER_ALREADY_EXISTS.getCode())
                .message(localeMessage.getLocaleMessage("exception.user.exists", locale))
                .build();
        request.getDescription(false);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        log.error("Error, user not found", ex);
        Locale locale = getLocaleFromHeader(request);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .errorCode(ErrorCodes.USER_NOT_FOUND.getCode())
                .message(localeMessage.getLocaleMessage("exception.user.notfound", locale))
                .build();
        request.getDescription(false);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        log.error("Error, authentication exception", ex);
        Locale locale = getLocaleFromHeader(request);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .errorCode(ErrorCodes.UNAUTHORIZED.getCode())
                .message(localeMessage.getLocaleMessage("exception.unauthorized", locale))
                .build();
        request.getDescription(false);

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        log.error("Illegal argument exception", ex);
        Locale locale = getLocaleFromHeader(request);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errorCode(ErrorCodes.ILLEGAL_ARGUMENT.getCode())
                .message(localeMessage.getLocaleMessage("exception.task.illegalargument", locale))
                .build();
        request.getDescription(false);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, WebRequest request) {
        log.error("Error, something went wrong", ex);
        Locale locale = getLocaleFromHeader(request);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorCode(ErrorCodes.INTERNAL_SERVER_ERROR.getCode())
                .message(localeMessage.getLocaleMessage("exception.internalServerError", locale))
                .build();
        request.getDescription(false);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        log.error("Error, bad request", ex);
        Locale locale = getLocaleFromHeader(request);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errorCode(ErrorCodes.BAD_REQUEST.getCode())
                .message(localeMessage.getLocaleMessage("exception.badRequest", locale))
                .build();
        request.getDescription(false);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

    public Locale getLocaleFromHeader(WebRequest request) {

        String langHeader = request.getHeader("Accept-Language");
        return langHeader != null ? Locale.forLanguageTag(langHeader) : Locale.getDefault();

    }
}
