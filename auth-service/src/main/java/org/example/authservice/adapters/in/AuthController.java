package org.example.authservice.adapters.in;

import io.jsonwebtoken.Claims;
import lombok.Data;
import org.example.authservice.application.port.in.AuthUseCase;
import org.example.authservice.application.port.in.SignupUseCase;
import org.example.authservice.application.port.in.dto.AuthResponse;
import org.example.authservice.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
        AuthResponse response = authUseCase.authenticate(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupRequest request) {
        signupUseCase.register(request.getName(), request.getEmail(), request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Data
    public static class SignupRequest {
        private String name;
        private String email;
        private String password;
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validate(@RequestParam("token") String token) {
        if (!jwtProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Claims claims = jwtProvider.getClaims(token);

        Map<String, Object> response = new HashMap<>();
        response.put("sub", claims.getSubject());
        response.put("email", claims.get("email"));
        response.put("exp", claims.getExpiration().getTime());

        return ResponseEntity.ok(response);
    }
}
