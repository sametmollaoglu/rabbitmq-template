package com.example.rabbitmq_tutorial.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ Configuration class that sets up exchanges, queues, and their
 * bindings.
 * This configuration demonstrates different types of exchanges (Direct, Topic,
 * Fanout)
 * and implements Dead Letter Queue (DLQ) pattern for handling failed messages.
 */
@Configuration
public class RabbitMQConfig {

    // Constants for queue and exchange names
    public static final String QUEUE_NAME = "queueSamet";
    public static final String DLX_NAME = "deadLetterExchangeSamet";
    public static final String DLQ_MESSAGES_QUEUE = "queueSamet.dlq";
    public static final String EXCHANGE_NAME = "exchangeSamet";
    public static final String ROUTING_KEY = "routingKeySamet";
    public static final String DLQ_ROUTING_KEY = "dlqRoutingKeySamet";

    /**
     * Creates the main queue with Dead Letter Exchange configuration.
     * Messages that fail processing will be redirected to DLQ.
     * 
     * @return Queue configured with DLX settings
     */
    @Bean
    public Queue queue() {
        return QueueBuilder.durable(QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", DLX_NAME)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .build();
    }

    /**
     * Creates a queue specifically for error logs.
     * Used with Topic Exchange to capture error-specific messages.
     */
    @Bean
    public Queue errorQueue() {
        return new Queue("errorQueue", true);
    }

    /**
     * Creates a queue that captures all types of logs.
     * Used with Topic Exchange to capture all log messages using wildcard routing.
     */
    @Bean
    public Queue allLogsQueue() {
        return new Queue("allLogsQueue", true);
    }

    /**
     * Creates a queue for SMS notifications.
     * Part of the Fanout Exchange pattern for broadcast messaging.
     */
    @Bean
    public Queue smsQueue() {
        return new Queue("smsQueue", true);
    }

    /**
     * Creates a queue for email notifications.
     * Part of the Fanout Exchange pattern for broadcast messaging.
     */
    @Bean
    public Queue emailQueue() {
        return new Queue("emailQueue", true);
    }

    /**
     * Creates a queue for push notifications.
     * Part of the Fanout Exchange pattern for broadcast messaging.
     */
    @Bean
    public Queue pushNotificationQueue() {
        return new Queue("pushNotificationQueue", true);
    }

    /**
     * Creates the Dead Letter Queue (DLQ) for handling failed messages.
     * Messages that fail processing in the main queue will be redirected here.
     */
    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DLQ_MESSAGES_QUEUE, true);
    }

    /**
     * Creates a Direct Exchange for point-to-point messaging.
     * Messages are routed to queues based on exact routing key matches.
     */
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    /**
     * Creates a Topic Exchange for pattern-based message routing.
     * Supports wildcard routing patterns (*, #) for flexible message distribution.
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topicExchangeSamet");
    }

    /**
     * Creates a Fanout Exchange for broadcast messaging.
     * Messages sent to this exchange are distributed to all bound queues.
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchangeSamet");
    }

    /**
     * Creates the Dead Letter Exchange for handling failed messages.
     */
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLX_NAME);
    }

    /**
     * Binds the main queue to the Direct Exchange.
     * Messages will be routed based on exact routing key matches.
     */
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    /**
     * Binds the error queue to the Topic Exchange.
     * Captures messages with routing key "log.error".
     */
    @Bean
    public Binding bindErrorQueue() {
        return BindingBuilder.bind(errorQueue()).to(topicExchange()).with("log.error");
    }

    /**
     * Binds the all-logs queue to the Topic Exchange.
     * Captures all messages with routing keys matching "log.#" pattern.
     */
    @Bean
    public Binding bindAllLogsQueue() {
        return BindingBuilder.bind(allLogsQueue()).to(topicExchange()).with("log.#");
    }

    /**
     * Binds the SMS queue to the Fanout Exchange.
     * Will receive all messages published to the Fanout Exchange.
     */
    @Bean
    public Binding bindSmsQueue() {
        return BindingBuilder.bind(smsQueue()).to(fanoutExchange());
    }

    /**
     * Binds the email queue to the Fanout Exchange.
     * Will receive all messages published to the Fanout Exchange.
     */
    @Bean
    public Binding bindEmailQueue() {
        return BindingBuilder.bind(emailQueue()).to(fanoutExchange());
    }

    /**
     * Binds the push notification queue to the Fanout Exchange.
     * Will receive all messages published to the Fanout Exchange.
     */
    @Bean
    public Binding bindPushNotificationQueue() {
        return BindingBuilder.bind(pushNotificationQueue()).to(fanoutExchange());
    }

    /**
     * Binds the Dead Letter Queue to the Dead Letter Exchange.
     * Failed messages will be routed here using the DLQ routing key.
     */
    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DLQ_ROUTING_KEY);
    }

    /**
     * Configures message conversion to JSON format.
     * Enables automatic conversion of messages to and from JSON format.
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
