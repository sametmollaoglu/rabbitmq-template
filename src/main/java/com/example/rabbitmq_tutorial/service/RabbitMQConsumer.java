package com.example.rabbitmq_tutorial.service;

import com.example.rabbitmq_tutorial.model.Message;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.example.rabbitmq_tutorial.config.RabbitMQConfig.DLQ_MESSAGES_QUEUE;
import static com.example.rabbitmq_tutorial.config.RabbitMQConfig.QUEUE_NAME;

@Service
public class RabbitMQConsumer {

    // Direct Exchange tarafından gönderilen mesajları "queueSamet" kuyruğundan tüketir
    @RabbitListener(queues = QUEUE_NAME)
    public void consumeMessage(Message message) {
            System.out.println("📩 Mesaj Alındı: " + message.getMessage());
            if (message.getMessage().contains("fail")) {
                System.out.println("⚠️ Hata Alındı, Mesaj DLQ'ya Gönderiliyor: " + message.getMessage());
                throw new AmqpRejectAndDontRequeueException("⛔ Simüle Edilmiş Hata!");
        }
    }


    // DLQ'ya düşen mesajları dinler
    @RabbitListener(queues = DLQ_MESSAGES_QUEUE)
    public void consumeDeadLetterQueue(Message message) {
        System.out.println("⚠️ DLQ'ya Düşen Mesaj: " + message.getMessage());
    }


    // Topic Exchange üzerinden sadece "log.error" ile gelen mesajları tüketir
    @RabbitListener(queues = "errorQueue")
    public void consumeErrorLogs(String message) {
        System.out.println("🚨 Error Log Alındı: " + message);
    }

    // Topic Exchange üzerinden "log.#" pattern'ine uyan tüm mesajları tüketir
    @RabbitListener(queues = "allLogsQueue")
    public void consumeAllLogs(String message) {
        System.out.println("📝 Genel Log Alındı: " + message);
    }


    // Fanout Exchange üzerinden gelen mesajları tüketir
    @RabbitListener(queues = "smsQueue")
    public void consumeSms(String message) {
        System.out.println("📩 SMS Gönderildi: " + message);
    }

    // Fanout Exchange üzerinden gelen mesajları tüketir
    @RabbitListener(queues = "emailQueue")
    public void consumeEmail(String message) {
        System.out.println("📧 E-Posta Gönderildi: " + message);
    }

    // Fanout Exchange üzerinden gelen mesajları tüketir
    @RabbitListener(queues = "pushNotificationQueue")
    public void consumePushNotification(String message) {
        System.out.println("🔔 Push Bildirim Gönderildi: " + message);
    }
}
