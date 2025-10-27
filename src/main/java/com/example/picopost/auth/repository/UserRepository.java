package com.example.picopost.auth.repository;

import com.example.picopost.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Spring Data automatically implements this: SELECT * FROM users WHERE username = ?
    Optional<User> findByUsername(String username);
}