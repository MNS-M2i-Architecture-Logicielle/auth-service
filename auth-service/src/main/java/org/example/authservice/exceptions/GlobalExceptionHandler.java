package org.example.authservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler to manage application-wide exceptions.
 *
 * Handles specific exceptions and maps them to appropriate HTTP responses.
 * This class uses Spring's @ControllerAdvice to intercept exceptions
 * thrown by controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link BadCredentialsException} thrown during authentication failures.
     * Returns an HTTP 401 Unauthorized response with the exception message as the body.
     *
     * @param ex the BadCredentialsException instance
     * @return a ResponseEntity containing the error message and HTTP status 401
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
