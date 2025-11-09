package com.example.picopost.user.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserRabbitMQConfig {
    
    public static final String NOTIFICATION_QUEUE = "user.notification.queue";
    public static final String NOTIFICATION_ROUTING_KEY = "notification.received";

    // 1. Define the unique queue for the User Service
    @Bean
    public Queue userNotificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true); 
    }

    // 2. Define the unique binding to the central exchange
    // Spring injects the single TopicExchange bean (defined in RabbitMQCentralConfig)
    @Bean
    public Binding userNotificationBinding(Queue userNotificationQueue, TopicExchange userEventsExchange) {
        // You would use a different exchange name if listening to a different service's events
        return BindingBuilder.bind(userNotificationQueue).to(userEventsExchange).with(NOTIFICATION_ROUTING_KEY);
    }
    
}
