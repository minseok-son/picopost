package com.example.picopost.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.picopost.user.model.User;
import com.example.picopost.user.repository.UserRepository;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long registerUser(String username, String password) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        
        return userRepository.save(newUser).getId();
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
