package org.example.authservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for user login requests.
 *
 * Contains the email and password required for user authentication.
 * Validation annotations ensure that email is a valid format and
 * neither field is null.
 */
@Getter
@Setter
public class LoginRequest {

    /**
     * The user's email address.
     * Must be a valid email format and cannot be null.
     */
    @NotNull
    @Email
    private String email;

    /**
     * The user's password.
     * Cannot be null.
     */
    @NotNull
    private String password;
}
