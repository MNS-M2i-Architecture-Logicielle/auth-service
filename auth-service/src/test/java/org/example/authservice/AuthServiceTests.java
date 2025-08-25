package org.example.authservice;

import org.example.authservice.application.port.in.dto.AuthResponse;
import org.example.authservice.application.port.out.ClientRepository;
import org.example.authservice.application.service.AuthService;
import org.example.authservice.domain.Client;
import org.example.authservice.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTests {

    private ClientRepository clientRepository;
    private PasswordEncoder passwordEncoder;
    private JwtProvider jwtProvider;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        clientRepository = mock(ClientRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtProvider = mock(JwtProvider.class);
        authService = new AuthService(clientRepository, passwordEncoder, jwtProvider);
    }

    @Test
    void authenticate_validCredentials_returnsAuthResponse() {
        Client client = new Client();
        client.setId(1L);
        client.setEmail("test@test.com");
        client.setPassword("hashedPassword");

        when(clientRepository.findByEmail("test@test.com")).thenReturn(Optional.of(client));
        when(passwordEncoder.matches("password", "hashedPassword")).thenReturn(true);
        when(jwtProvider.generateToken(client)).thenReturn("jwt-token");
        when(jwtProvider.getExpirationMs()).thenReturn(300000L);

        AuthResponse response = authService.authenticate("test@test.com", "password");

        assertNotNull(response);
        assertEquals("jwt-token", response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(300000L, response.getExpiresInMs());
        verify(clientRepository).findByEmail("test@test.com");
        verify(jwtProvider).generateToken(client);
    }

    @Test
    void authenticate_invalidEmail_throwsException() {
        when(clientRepository.findByEmail("bad@test.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> authService.authenticate("bad@test.com", "password"));
    }

    @Test
    void authenticate_invalidPassword_throwsException() {
        Client client = new Client();
        client.setId(1L);
        client.setEmail("test@test.com");
        client.setPassword("hashedPassword");

        when(clientRepository.findByEmail("test@test.com")).thenReturn(Optional.of(client));
        when(passwordEncoder.matches("wrong", "hashedPassword")).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> authService.authenticate("test@test.com", "wrong"));
    }

    @Test
    void authenticate_nullPasswordInClient_throwsException() {
        Client client = new Client();
        client.setId(1L);
        client.setEmail("test@test.com");
        client.setPassword(null);

        when(clientRepository.findByEmail("test@test.com")).thenReturn(Optional.of(client));

        assertThrows(IllegalArgumentException.class,
                () -> authService.authenticate("test@test.com", "password"));
    }

    @Test
    void register_newEmail_savesClient() {
        when(clientRepository.findByEmail("new@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("hashed");

        authService.register("new@test.com", "password");

        verify(clientRepository).save(argThat(client ->
                client.getEmail().equals("new@test.com")
                        && client.getPassword().equals("hashed")));
    }

    @Test
    void register_existingEmail_throwsException() {
        Client existing = new Client();
        existing.setEmail("existing@test.com");
        when(clientRepository.findByEmail("existing@test.com")).thenReturn(Optional.of(existing));

        assertThrows(IllegalStateException.class,
                () -> authService.register("existing@test.com", "password"));
    }
}
