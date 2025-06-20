package org.example.authservice.services;

import org.example.authservice.dtos.LoginRequest;
import org.example.authservice.dtos.RegisterRequest;
import org.example.authservice.entities.User;
import org.example.authservice.exceptions.BadCredentialsException;
import org.example.authservice.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class responsible for user-related business logic,
 * such as registration and authentication.
 */
@Service
public class UserService {

    /**
     * Repository interface for accessing user data.
     */
    private final UserRepository userRepository;

    /**
     * Constructs a new UserService with the specified UserRepository.
     *
     * @param userRepository the repository for user persistence operations
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers and saves a new user based on the provided registration request.
     *
     * @param request the registration data including email and password
     * @return the saved User entity
     */
    public User save(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return userRepository.save(user);
    }

    /**
     * Authenticates a user with the provided login request.
     *
     * Checks if the user exists and if the password matches.
     * Throws a BadCredentialsException if authentication fails.
     *
     * @param request the login data including email and password
     * @return a simple token string composed of hashed email and password
     * @throws BadCredentialsException if email or password is invalid
     */
    public String login(LoginRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (user.isEmpty() || !user.get().getPassword().equals(request.getPassword()))
            throw new BadCredentialsException("Invalid username or password");

        return user.get().getEmail().hashCode() + ":" + user.get().getPassword().hashCode();
    }
}
