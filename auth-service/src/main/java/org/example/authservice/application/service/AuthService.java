package org.example.authservice.application.service;

import org.example.authservice.adapters.in.dto.ClientCreationRequest;
import org.example.authservice.application.port.in.AuthUseCase;
import org.example.authservice.application.port.in.SignupUseCase;
import org.example.authservice.application.port.in.dto.AuthResponse;
import org.example.authservice.application.port.out.ClientRepository;
import org.example.authservice.domain.Client;
import org.example.authservice.security.JwtProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthUseCase, SignupUseCase {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthService(ClientRepository clientRepository,
                       PasswordEncoder passwordEncoder,
                       JwtProvider jwtProvider) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public AuthResponse authenticate(String email, String password) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (client.getPassword() == null) {
            throw new IllegalArgumentException("No password set for this client");
        }

        if (!passwordEncoder.matches(password, client.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = jwtProvider.generateToken(client);
        long expiresIn = jwtProvider.getExpirationMs();

        return new AuthResponse(token, "Bearer", expiresIn);
    }

    @Override
    public void register(String email, String password) {
        if (clientRepository.findByEmail(email).isPresent())
            throw new IllegalStateException("Client with this email already exists");

        Client request = new Client();
        request.setEmail(email);
        request.setPassword(passwordEncoder.encode(password));

        clientRepository.save(request);
    }
}