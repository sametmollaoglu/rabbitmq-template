package com.example.rabbitmq_tutorial.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "queueSamet";
    public static final String DLX_NAME = "deadLetterExchangeSamet";
    public static final String DLQ_MESSAGES_QUEUE = "queueSamet.dlq";
    public static final String EXCHANGE_NAME = "exchangeSamet";
    public static final String ROUTING_KEY = "routingKeySamet";
    public static final String DLQ_ROUTING_KEY = "dlqRoutingKeySamet";

    // Ana kuyruğu oluşturur (Direct Exchange için kullanılır)
    @Bean
    public Queue queue() {
        return QueueBuilder.durable(QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", DLX_NAME) // Bind queue to DLX
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY) // Route to DLQ
                .build();
    }

    // Error logları için ayrı bir kuyruk oluşturur (Topic Exchange kullanacak)
    @Bean
    public Queue errorQueue() {
        return new Queue("errorQueue", true);
    }

    // Tüm logları dinleyecek genel bir kuyruk oluşturur (Topic Exchange kullanacak)
    @Bean
    public Queue allLogsQueue() {
        return new Queue("allLogsQueue", true);
    }

    // SMS gönderimleri için ayrı bir kuyruk oluşturur (Fanout Exchange kullanacak)
    @Bean
    public Queue smsQueue() {
        return new Queue("smsQueue", true);
    }

    // E-posta gönderimleri için ayrı bir kuyruk oluşturur (Fanout Exchange kullanacak)
    @Bean
    public Queue emailQueue() {
        return new Queue("emailQueue", true);
    }

    // Push bildirimleri için ayrı bir kuyruk oluşturur (Fanout Exchange kullanacak)
    @Bean
    public Queue pushNotificationQueue() {
        return new Queue("pushNotificationQueue", true);
    }

    // DLQ (Dead Letter Queue) tanımlıyoruz
    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DLQ_MESSAGES_QUEUE, true);
    }


    // Direct Exchange oluşturur (Routing Key tam eşleşme ile çalışır)
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    // Topic Exchange oluşturur (Routing Key desenleriyle çalışır)
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topicExchangeSamet");
    }

    // Fanout Exchange oluşturur (Tüm kuyruklara mesajı gönderir)
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchangeSamet");
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLX_NAME);
    }


    // Ana kuyruğu Direct Exchange'e bağlar (Sadece tam eşleşen Routing Key ile çalışır)
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    // "errorQueue" kuyruğunu Topic Exchange'e bağlar (Sadece "log.error" mesajlarını alır)
    @Bean
    public Binding bindErrorQueue() {
        return BindingBuilder.bind(errorQueue()).to(topicExchange()).with("log.error");
    }

    // "allLogsQueue" kuyruğunu Topic Exchange'e bağlar (Tüm "log.#" pattern'lerine uyan mesajları alır)
    @Bean
    public Binding bindAllLogsQueue() {
        return BindingBuilder.bind(allLogsQueue()).to(topicExchange()).with("log.#");
    }

    // SMS kuyruğunu Fanout Exchange'e bağlar (Tüm kuyruklara mesajı gönderir)
    @Bean
    public Binding bindSmsQueue() {
        return BindingBuilder.bind(smsQueue()).to(fanoutExchange());
    }

    // E-posta kuyruğunu Fanout Exchange'e bağlar (Tüm kuyruklara mesajı gönderir)
    @Bean
    public Binding bindEmailQueue() {
        return BindingBuilder.bind(emailQueue()).to(fanoutExchange());
    }

    // Push bildirim kuyruğunu Fanout Exchange'e bağlar (Tüm kuyruklara mesajı gönderir)
    @Bean
    public Binding bindPushNotificationQueue() {
        return BindingBuilder.bind(pushNotificationQueue()).to(fanoutExchange());
    }

    // Bind the DLQ to the Dead Letter Exchange
    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DLQ_ROUTING_KEY);
    }


    // Mesajları JSON formatında göndermek ve almak için JSON Converter tanımlar
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
