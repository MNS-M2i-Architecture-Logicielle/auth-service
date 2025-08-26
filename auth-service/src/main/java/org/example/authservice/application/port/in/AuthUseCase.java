package org.example.authservice.application.port.in;


import org.example.authservice.application.port.in.dto.AuthResponse;

public interface AuthUseCase {
    AuthResponse authenticate(String email, String password);
}
