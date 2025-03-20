package com.example.rabbitmq_tutorial.controller;

import com.example.rabbitmq_tutorial.model.Message;
import com.example.rabbitmq_tutorial.service.RabbitMQProducer;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for handling RabbitMQ message publishing operations.
 * Provides endpoints for sending messages to different types of exchanges:
 * - Direct Exchange (point-to-point messaging)
 * - Topic Exchange (pattern-based routing)
 * - Fanout Exchange (broadcast messaging)
 */
@RestController
@RequestMapping("/api")
public class RabbitMQController {

    private final RabbitMQProducer producer;

    public RabbitMQController(RabbitMQProducer producer) {
        this.producer = producer;
    }

    /**
     * Sends a message to the Direct Exchange.
     * Messages will be routed to queues based on exact routing key matches.
     *
     * @param message The message object containing the content to be sent
     * @return String confirmation message
     */
    @PostMapping("/send/direct")
    public String sendMessageToDirect(@RequestBody Message message) {
        producer.sendMessageToDirectExchange(message);
        return "Direct Exchange'e Mesaj Gönderildi: " + message.getMessage();
    }

    /**
     * Sends a message to the Topic Exchange.
     * Messages will be routed to queues based on routing key patterns.
     *
     * @param routingKey The routing key used for message routing (supports
     *                   wildcards)
     * @param message    The message content to be sent
     * @return String confirmation message
     */
    @PostMapping("/send/topic")
    public String sendMessageToTopic(@RequestParam String routingKey, @RequestBody String message) {
        producer.sendMessageToTopicExchange(routingKey, message);
        return "Topic Exchange'e Mesaj Gönderildi (Routing Key: " + routingKey + ")";
    }

    /**
     * Sends a message to the Fanout Exchange.
     * Messages will be broadcast to all bound queues.
     *
     * @param message The message content to be broadcast
     * @return String confirmation message
     */
    @PostMapping("/send/fanout")
    public String sendMessageToFanout(@RequestBody String message) {
        producer.sendMessageToFanoutExchange(message);
        return "Fanout Exchange'e Mesaj Gönderildi!";
    }
}
