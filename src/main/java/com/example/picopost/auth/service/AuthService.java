package com.example.picopost.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.picopost.auth.dto.AuthResponse;
import com.example.picopost.auth.dto.CredentialRequest;
import com.example.picopost.auth.exception.DuplicateUsernameException;
import com.example.picopost.auth.model.UserPrincipal;
import com.example.picopost.auth.repository.UserPrincipalRepository;
import com.example.picopost.auth.security.JwtUtils;
import com.example.picopost.user.service.UserService;

@Service
@Transactional
public class AuthService {
    private final UserPrincipalRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;


    public AuthService(UserPrincipalRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtils jwtUtils,
                       UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    public AuthResponse authenticateUser(CredentialRequest loginRequest) {
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
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();

        // 5. Return AuthResponse with token and user info
        return new AuthResponse(jwt, userDetails.getId(), userDetails.getUsername());
    }

    public Long registerUser(CredentialRequest signUpRequest) {
        userRepository.findByUsername(signUpRequest.getUsername()).ifPresent(u -> {
            throw new DuplicateUsernameException("Username '" + signUpRequest.getUsername() + "' is already taken");
        });

        // 2. Security action: HASH THE PASSWORD
        String hashedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        
        // 3. 💥 DELEGATE PERSISTENCE 💥 (Internal synchronous call)
        // The UserService handles saving the core User entity.
        Long newUserId = userService.registerUser(signUpRequest.getUsername(), hashedPassword);
        
        // 4. Return the new User ID (obtained from the UserService)
        return newUserId;
    }
}
