package com.example.picopost.auth.service;

import com.example.picopost.auth.model.User;
import com.example.picopost.auth.repository.UserRepository;
import com.example.picopost.auth.security.JwtUtils;
import com.example.picopost.auth.dto.LoginRequest;
import com.example.picopost.auth.dto.AuthResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        // 1. Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // 2. Set authentication in context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generate JWT token
        String jwt = jwtUtils.generateToken(authentication);

        // 4. Get user details
        User userDetails = (User) authentication.getPrincipal();

        // 5. Return AuthResponse with token and user info
        return new AuthResponse(jwt, userDetails.getId(), userDetails.getUsername());
    }

    public User registerUser(LoginRequest signUpRequest) {
        // Check if username is already taken
        Optional<User> existingUser = userRepository.findByUsername(signUpRequest.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Username is already taken.");
        }

        // Create new user account
        User newUser = new User();
        newUser.setUsername(signUpRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        // Save user to the database
        return userRepository.save(newUser);
    }
    
}
