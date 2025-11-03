package com.example.picopost.auth.controller;

import com.example.picopost.auth.dto.CredentialRequest;
import com.example.picopost.auth.dto.AuthResponse;
import com.example.picopost.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody CredentialRequest loginRequest) {
        AuthResponse authResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody CredentialRequest registerRequest) {
        Long userId = authService.registerUser(registerRequest).getId();
        return ResponseEntity.ok(userId);
    }

    @DeleteMapping("/delete//{userId}")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        authService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
