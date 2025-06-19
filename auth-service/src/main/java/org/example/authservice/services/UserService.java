package org.example.authservice.services;

import org.example.authservice.dtos.LoginRequest;
import org.example.authservice.dtos.RegisterRequest;
import org.example.authservice.entities.User;
import org.example.authservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(RegisterRequest request) {

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return userRepository.save(user);
    }

    public String login(LoginRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (user.isEmpty() || !user.get().getPassword().equals(request.getPassword()))
            return "Invalid email or password";

        return "Authentification réussi";
    }
}
