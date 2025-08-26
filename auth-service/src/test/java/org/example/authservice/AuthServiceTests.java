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
        client.setMail("test@test.com");
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
    void authenticate_nullPassword_throwsException() {
        Client client = new Client();
        client.setMail("test@test.com");
        client.setPassword(null);

        when(clientRepository.findByEmail("test@test.com")).thenReturn(Optional.of(client));

        assertThrows(IllegalArgumentException.class,
                () -> authService.authenticate("test@test.com", "password"));
    }

    @Test
    void authenticate_wrongPassword_throwsException() {
        Client client = new Client();
        client.setMail("test@test.com");
        client.setPassword("hashedPassword");

        when(clientRepository.findByEmail("test@test.com")).thenReturn(Optional.of(client));
        when(passwordEncoder.matches("wrong", "hashedPassword")).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> authService.authenticate("test@test.com", "wrong"));
    }

    @Test
    void register_newClient_createsClientWithEncodedPassword() {
        when(passwordEncoder.encode("plain")).thenReturn("hashed");

        authService.register("John Doe", "john@test.com", "plain");

        verify(clientRepository).createClient(argThat(req ->
                req.getName().equals("John Doe")
                        && req.getMail().equals("john@test.com")
                        && req.getPassword().equals("hashed")));
    }

    @Test
    void register_encodesPasswordBeforeSaving() {
        when(passwordEncoder.encode("12345")).thenReturn("ENCODED");

        authService.register("Jane", "jane@test.com", "12345");

        verify(clientRepository).createClient(argThat(req ->
                req.getPassword().equals("ENCODED")));
    }
}
