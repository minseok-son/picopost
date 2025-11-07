package com.example.picopost.auth.repository;

import com.example.picopost.auth.model.UserPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPrincipalRepository extends JpaRepository<UserPrincipal, Long> {
    
    // Spring Data automatically implements this: SELECT * FROM users WHERE username = ?
    Optional<UserPrincipal> findByUsername(String username);
}