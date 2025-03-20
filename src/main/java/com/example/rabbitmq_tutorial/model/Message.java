package com.example.rabbitmq_tutorial.model;

import java.io.Serializable;

/**
 * Message model class representing the structure of messages
 * sent through RabbitMQ exchanges.
 * Implements Serializable to support message conversion and transmission.
 */
public class Message implements Serializable {
    private String message;

    public Message() {
    }

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
