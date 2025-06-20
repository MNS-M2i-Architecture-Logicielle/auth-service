package org.example.authservice.controllers;

import org.example.authservice.dtos.LoginRequest;
import org.example.authservice.dtos.RegisterRequest;
import org.example.authservice.entities.User;
import org.example.authservice.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling user authentication requests.
 * Provides endpoints for user registration and login.
 * All endpoints are prefixed with "/api/auth".
 * This controller delegates business logic to the UserService.
 */
@RestController
@RequestMapping("/api/auth")
public class UserController {

        /**
         * Service layer for user-related operations.
         */
        private final UserService userService;

        /**
         * Constructs a new UserController with the specified UserService.
         *
         * @param userService the user service to handle business logic
         */
        public UserController(UserService userService){
                this.userService = userService;
        }

        /**
         * Registers a new user based on the provided registration request.
         *
         * @param registerRequest the registration data including user details
         * @return the created User entity
         */
        @PostMapping("/register")
        public User register(@RequestBody RegisterRequest registerRequest) {
                return this.userService.save(registerRequest);
        }

        /**
         * Authenticates a user with the provided login request.
         *
         * @param loginRequest the login data including email and password
         * @return a token or message indicating login success or failure
         */
        @PostMapping("/login")
        public String login(@RequestBody LoginRequest loginRequest) {
                return this.userService.login(loginRequest);
        }
}
