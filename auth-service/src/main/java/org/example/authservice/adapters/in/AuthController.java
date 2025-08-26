package org.example.authservice.adapters.in;

import lombok.Data;
import org.example.authservice.adapters.in.dto.request.LoginRequest;
import org.example.authservice.adapters.in.dto.request.SignupRequest;
import org.example.authservice.application.port.in.AuthUseCase;
import org.example.authservice.application.port.in.SignupUseCase;
import org.example.authservice.application.port.in.dto.AuthResponse;
import org.example.authservice.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthUseCase authUseCase;
    private final SignupUseCase signupUseCase;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthController(AuthUseCase authUseCase, SignupUseCase signupUseCase, JwtProvider jwtProvider) {
        this.authUseCase = authUseCase;
        this.signupUseCase = signupUseCase;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authUseCase.authenticate(request.getMail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupRequest request) {
        signupUseCase.register(request.getName(), request.getMail(), request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validate(@RequestBody String token) {
        if (!jwtProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().build();
    }
}
