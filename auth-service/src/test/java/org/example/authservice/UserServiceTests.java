package org.example.authservice;

import org.example.authservice.dtos.LoginRequest;
import org.example.authservice.dtos.RegisterRequest;
import org.example.authservice.entities.User;
import org.example.authservice.exceptions.BadCredentialsException;
import org.example.authservice.repositories.UserRepository;
import org.example.authservice.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void save_shouldReturnSavedUser() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("john@example.com");
        request.setPassword("root");

        User savedUser = new User();
        savedUser.setEmail(request.getEmail());
        savedUser.setPassword(request.getPassword());

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.save(request);

        assertEquals("john@example.com", result.getEmail());
        assertEquals("root", result.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void login_shouldReturnLogin_correct() {
        LoginRequest request = new LoginRequest();
        request.setEmail("john@example.com");
        request.setPassword("root");

        User user = new User();
        user.setEmail("john@example.com");
        user.setPassword("root");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        String result = userService.login(request);

        String expected = user.getEmail().hashCode() + ":" + user.getPassword().hashCode();
        assertEquals(expected, result);
        verify(userRepository, times(1)).findByEmail("john@example.com");
    }

    @Test
    void login_shouldThrowBadCredentialsException_whenPasswordIncorrect() {
        LoginRequest request = new LoginRequest();
        request.setEmail("john@example.com");
        request.setPassword("wrong");

        User user = new User();
        user.setEmail("john@example.com");
        user.setPassword("correct");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        assertThrows(BadCredentialsException.class, () -> userService.login(request));
    }

}
