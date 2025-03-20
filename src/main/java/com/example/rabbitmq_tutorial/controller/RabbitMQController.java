package com.example.rabbitmq_tutorial.controller;

import com.example.rabbitmq_tutorial.model.Message;
import com.example.rabbitmq_tutorial.service.RabbitMQProducer;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RabbitMQController {

    private final RabbitMQProducer producer;

    public RabbitMQController(RabbitMQProducer producer) {
        this.producer = producer;
    }

    // Direct Exchange'e mesaj gönder
    @PostMapping("/send/direct")
    public String sendMessageToDirect(@RequestBody Message message) {
        producer.sendMessageToDirectExchange(message);
        return "Direct Exchange'e Mesaj Gönderildi: " + message.getMessage();
    }

    // Topic Exchange'e mesaj gönder
    @PostMapping("/send/topic")
    public String sendMessageToTopic(@RequestParam String routingKey, @RequestBody String message) {
        producer.sendMessageToTopicExchange(routingKey, message);
        return "Topic Exchange'e Mesaj Gönderildi (Routing Key: " + routingKey + ")";
    }

    // Fanout Exchange'e mesaj gönder
    @PostMapping("/send/fanout")
    public String sendMessageToFanout(@RequestBody String message) {
        producer.sendMessageToFanoutExchange(message);
        return "Fanout Exchange'e Mesaj Gönderildi!";
    }
}
