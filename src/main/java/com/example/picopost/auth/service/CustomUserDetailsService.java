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

    // NOTE: This implementation uses the database ID (String version) as the 'username'
    // in the context of the JWT flow, but can easily be adapted to use the actual username.
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String idString) throws UsernameNotFoundException {
        Long id = Long.parseLong(idString);
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }
}
