package com.example.rabbitmq_tutorial.service;

import com.example.rabbitmq_tutorial.model.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Service;

import static com.example.rabbitmq_tutorial.config.RabbitMQConfig.EXCHANGE_NAME;
import static com.example.rabbitmq_tutorial.config.RabbitMQConfig.ROUTING_KEY;

/**
 * Service class responsible for publishing messages to RabbitMQ exchanges.
 * Supports publishing to different types of exchanges:
 * - Direct Exchange (point-to-point messaging)
 * - Topic Exchange (pattern-based routing)
 * - Fanout Exchange (broadcast messaging)
 */
@Service
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Constructs a new RabbitMQProducer with the specified RabbitTemplate.
     * Configures JSON message conversion for all messages.
     *
     * @param rabbitTemplate The RabbitMQ template for message operations
     */
    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }

    /**
     * Sends a message to the Direct Exchange.
     * Messages will be routed to queues based on exact routing key matches.
     *
     * @param message The message object to be sent
     */
    public void sendMessageToDirectExchange(Message message) {
        System.out.println("📤 Direct Exchange'e Mesaj Gönderiliyor: " + message.getMessage());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
        System.out.println("✅ Direct Exchange Mesaj Gönderildi!");
    }

    /**
     * Sends a message to the Topic Exchange.
     * Messages will be routed to queues based on routing key patterns.
     *
     * @param routingKey The routing key used for message routing (supports
     *                   wildcards)
     * @param message    The message content to be sent
     */
    public void sendMessageToTopicExchange(String routingKey, String message) {
        System.out.println("📤 Topic Exchange'e Mesaj Gönderiliyor: " + message);
        rabbitTemplate.convertAndSend("topicExchangeSamet", routingKey, message);
        System.out.println("✅ Topic Exchange Mesaj Gönderildi!");
    }

    /**
     * Sends a message to the Fanout Exchange.
     * Messages will be broadcast to all bound queues regardless of routing key.
     *
     * @param message The message content to be broadcast
     */
    public void sendMessageToFanoutExchange(String message) {
        System.out.println("📤 Fanout Exchange'e Mesaj Gönderiliyor: " + message);
        rabbitTemplate.convertAndSend("fanoutExchangeSamet", "", message);
        System.out.println("✅ Fanout Exchange Mesaj Gönderildi!");
    }
}
