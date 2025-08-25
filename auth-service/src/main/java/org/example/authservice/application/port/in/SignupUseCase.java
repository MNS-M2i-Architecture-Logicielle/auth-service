package org.example.authservice.application.port.in;

public interface SignupUseCase {
    void register(String email, String password);
}
