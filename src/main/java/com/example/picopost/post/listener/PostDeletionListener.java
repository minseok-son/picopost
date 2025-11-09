package com.example.picopost.post.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.picopost.post.config.PostRabbitMQConfig;
import com.example.picopost.post.repository.PostRepository;

import jakarta.transaction.Transactional;

@Component
public class PostDeletionListener {

    private final PostRepository postRepository;

    public PostDeletionListener(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @RabbitListener(queues = PostRabbitMQConfig.POST_DELETE_QUEUE)
    @Transactional
    public void handleUserDeletion(String userIdString) {
        try {
            Long userId = Long.parseLong(userIdString);

            postRepository.deleteAllByUserId(userId);
        } catch (NumberFormatException e) {

        }
    }
    
}
