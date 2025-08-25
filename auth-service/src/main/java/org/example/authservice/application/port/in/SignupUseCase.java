package org.example.authservice.application.port.in;

public interface SignupUseCase {
    void register(String name, String email, String password);
}
