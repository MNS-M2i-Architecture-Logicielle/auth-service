package org.example.authservice.controllers;


import org.example.authservice.dtos.LoginRequest;
import org.example.authservice.dtos.RegisterRequest;
import org.example.authservice.entities.User;
import org.example.authservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {

        @Autowired
        private UserService userService;

        @PostMapping("/register")
        public User register(@RequestBody RegisterRequest registerRequest) {
                return this.userService.save(registerRequest);
        }

        @PostMapping("/login")
        public String login(@RequestBody LoginRequest loginRequest) {
                return this.userService.login(loginRequest);
        }
}
