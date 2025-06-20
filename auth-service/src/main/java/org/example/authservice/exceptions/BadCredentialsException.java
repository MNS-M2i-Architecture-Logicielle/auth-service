package org.example.authservice.exceptions;

/**
 * Exception thrown when authentication fails due to invalid credentials.
 *
 * This is a runtime exception indicating that the provided username or password
 * is incorrect during login attempts.
 */
public class BadCredentialsException extends RuntimeException {

    /**
     * Constructs a new BadCredentialsException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public BadCredentialsException(String message) {
        super(message);
    }
}
