package com.bitso.challenge.controller;

import com.bitso.challenge.exception.NotAllowedException;
import com.bitso.challenge.security.jwt.InvalidJwtAuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerController {

    private final Logger log = LoggerFactory.getLogger(getClass());


    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleNonExistingUserException(UsernameNotFoundException ex, WebRequest request) {
        String message = "User not found";
        log.error(message, ex);
        Map<String, String> error = new HashMap<>();
        error.put(message, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        String message = "Incorrect password";
        log.error(message, ex);
        Map<String, String> error = new HashMap<>();
        error.put(message, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleNotAllowedException(NotAllowedException ex, WebRequest request) {
        String message = "Action not allowed";
        log.error(message, ex);
        Map<String, String> error = new HashMap<>();
        error.put(message, ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleInvalidJwtAuthenticationException(InvalidJwtAuthenticationException ex, WebRequest request) {
        String message = "Incorrect token";
        log.error(message, ex);
        Map<String, String> error = new HashMap<>();
        error.put(message, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

}
