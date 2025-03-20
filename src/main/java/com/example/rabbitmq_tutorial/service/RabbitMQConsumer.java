package com.example.rabbitmq_tutorial.service;

import com.example.rabbitmq_tutorial.model.Message;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.example.rabbitmq_tutorial.config.RabbitMQConfig.DLQ_MESSAGES_QUEUE;
import static com.example.rabbitmq_tutorial.config.RabbitMQConfig.QUEUE_NAME;

/**
 * Service class that handles consuming messages from various RabbitMQ queues.
 * Implements listeners for different types of queues:
 * - Direct Exchange queue
 * - Dead Letter Queue (DLQ)
 * - Topic Exchange queues (error logs and all logs)
 * - Fanout Exchange queues (SMS, email, push notifications)
 */
@Service
public class RabbitMQConsumer {

    /**
     * Consumes messages from the main queue connected to Direct Exchange.
     * If a message contains the word "fail", it will be rejected and sent to DLQ.
     *
     * @param message The received message object
     * @throws AmqpRejectAndDontRequeueException when message processing fails
     */
    @RabbitListener(queues = QUEUE_NAME)
    public void consumeMessage(Message message) {
        System.out.println("📩 Mesaj Alındı: " + message.getMessage());
        if (message.getMessage().contains("fail")) {
            System.out.println("⚠️ Hata Alındı, Mesaj DLQ'ya Gönderiliyor: " + message.getMessage());
            throw new AmqpRejectAndDontRequeueException("⛔ Simüle Edilmiş Hata!");
        }
    }

    /**
     * Consumes messages from the Dead Letter Queue (DLQ).
     * Handles messages that failed processing in the main queue.
     *
     * @param message The failed message that was redirected to DLQ
     */
    @RabbitListener(queues = DLQ_MESSAGES_QUEUE)
    public void consumeDeadLetterQueue(Message message) {
        System.out.println("⚠️ DLQ'ya Düşen Mesaj: " + message.getMessage());
    }

    /**
     * Consumes error log messages from the Topic Exchange.
     * Only processes messages with routing key "log.error".
     *
     * @param message The error log message
     */
    @RabbitListener(queues = "errorQueue")
    public void consumeErrorLogs(String message) {
        System.out.println("🚨 Error Log Alındı: " + message);
    }

    /**
     * Consumes all log messages from the Topic Exchange.
     * Processes messages matching the "log.#" routing pattern.
     *
     * @param message The log message
     */
    @RabbitListener(queues = "allLogsQueue")
    public void consumeAllLogs(String message) {
        System.out.println("📝 Genel Log Alındı: " + message);
    }

    /**
     * Consumes messages from the SMS queue connected to Fanout Exchange.
     * Part of the broadcast messaging pattern.
     *
     * @param message The message to be sent as SMS
     */
    @RabbitListener(queues = "smsQueue")
    public void consumeSms(String message) {
        System.out.println("📩 SMS Gönderildi: " + message);
    }

    /**
     * Consumes messages from the email queue connected to Fanout Exchange.
     * Part of the broadcast messaging pattern.
     *
     * @param message The message to be sent as email
     */
    @RabbitListener(queues = "emailQueue")
    public void consumeEmail(String message) {
        System.out.println("📧 E-Posta Gönderildi: " + message);
    }

    /**
     * Consumes messages from the push notification queue connected to Fanout
     * Exchange.
     * Part of the broadcast messaging pattern.
     *
     * @param message The message to be sent as push notification
     */
    @RabbitListener(queues = "pushNotificationQueue")
    public void consumePushNotification(String message) {
        System.out.println("🔔 Push Bildirim Gönderildi: " + message);
    }
}
