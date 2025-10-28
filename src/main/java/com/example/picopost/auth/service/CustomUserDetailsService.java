package com.example.picopost.auth.service;

import com.example.picopost.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Loads the User entity when given a user ID (or username, depending on config).
 * Required by Spring Security to bridge the User entity to the Security context.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * REQUIRED FOR LOGIN: Loads the user by the actual username string.
     * The String input here is the literal username provided by the client (e.g., "admin").
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find by the actual username, not by trying to parse it as a Long ID.
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    /**
     * USED FOR JWT VALIDATION: Loads the user by the numerical ID.
     * This is needed by the JwtAuthFilter to load the user details after token validation.
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }
}
