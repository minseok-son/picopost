package com.example.picopost.user.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.picopost.config.RabbitMQConfig;
import com.example.picopost.post.config.PostRabbitMQConfig;
import com.example.picopost.user.model.User;
import com.example.picopost.user.repository.UserRepository;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    public UserService(UserRepository userRepository, RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Long registerUser(String username, String password) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        
        return userRepository.save(newUser).getId();
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);

        String message = String.valueOf(userId);

        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME,
            PostRabbitMQConfig.ROUTING_KEY,
            message
        );
    }
}
