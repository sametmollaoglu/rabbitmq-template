package com.example.rabbitmq_tutorial.service;

import com.example.rabbitmq_tutorial.model.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Service;

import static com.example.rabbitmq_tutorial.config.RabbitMQConfig.EXCHANGE_NAME;
import static com.example.rabbitmq_tutorial.config.RabbitMQConfig.ROUTING_KEY;


@Service
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }

    // Producer, mesajı Direct Exchange'e belirlenen Routing Key ile gönderir
    public void sendMessageToDirectExchange(Message message) {
        System.out.println("📤 Direct Exchange'e Mesaj Gönderiliyor: " + message.getMessage());
        rabbitTemplate.convertAndSend( EXCHANGE_NAME, ROUTING_KEY, message);
        System.out.println("✅ Direct Exchange Mesaj Gönderildi!");
    }

    // Producer, mesajı Topic Exchange'e belirlenen Routing Key ile gönderir
    public void sendMessageToTopicExchange(String routingKey, String message) {
        System.out.println("📤 Topic Exchange'e Mesaj Gönderiliyor: " + message);
        rabbitTemplate.convertAndSend("topicExchangeSamet", routingKey, message);
        System.out.println("✅ Topic Exchange Mesaj Gönderildi!");
    }

    // Producer, mesajı Fanout Exchange'e gönderir (Routing Key kullanmaz)
    public void sendMessageToFanoutExchange(String message) {
        System.out.println("📤 Fanout Exchange'e Mesaj Gönderiliyor: " + message);
        rabbitTemplate.convertAndSend("fanoutExchangeSamet", "", message);
        System.out.println("✅ Fanout Exchange Mesaj Gönderildi!");
    }
}
