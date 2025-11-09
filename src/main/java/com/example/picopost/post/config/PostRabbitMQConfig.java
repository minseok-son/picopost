package com.example.picopost.post.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostRabbitMQConfig {

    // Name of the exchange where the event will be sent
    public static final String EXCHANGE_NAME = "user.events.exchange";
    
    // Name of the queue the Post Service will listen to
    public static final String POST_DELETE_QUEUE = "post.delete.user"; 
    
    // The routing key used to send the message
    public static final String ROUTING_KEY = "user.deleted";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue postDeleteQueue() {
        return new Queue(POST_DELETE_QUEUE, true);
    }

    @Bean
    public Binding postDeleteBinding(Queue postDeletQueue, TopicExchange exchange) {
        return BindingBuilder.bind(postDeletQueue).to(exchange).with(ROUTING_KEY);
    }
    
}
