# RabbitMQ Spring Boot Project Template Documentation

## Getting Started
1. Start the RabbitMQ container on Docker.
   * You can monitor RabbitMQ through the console at http://localhost:15672/

## Topics Covered in This Project
This project demonstrates how to manage message queues using RabbitMQ with Spring Boot. The following concepts are covered:

### 1. Exchange Types and Usage
#### a) Direct Exchange
- Messages are sent with a specific routing key and only reach the matching queue.
- Example Usage:
  - **POST** `http://localhost:8080/api/send/direct`
  - Body:
    ```json
    {
        "message":"Direct Exchange Test fail"
    }
    ```

#### b) Topic Exchange
- Routes messages based on a pattern in the routing key.
- Example Usage:
  - **POST** `http://localhost:8080/api/send/topic?routingKey=log.error`
  - Body:
    ```
    "Error Log Test Message"
    ```
  - **POST** `http://localhost:8080/api/send/topic?routingKey=log.info`
  - Body:
    ```
    "Info Log Test Message"
    ```

#### c) Fanout Exchange
- Sends a message to all bound queues.
- Example Usage:
  - **POST** `http://localhost:8080/api/send/fanout`
  - Body:
    ```
    "Fanout Test Message"
    ```

### 2. Processing Messages with Consumers
- **Direct Exchange** messages are consumed from the `queueSamet` queue.
- **Topic Exchange** processes log messages in `errorQueue` and `allLogsQueue`.
- **Fanout Exchange** routes messages to different consumers:
  - SMS notifications to `smsQueue`
  - Emails to `emailQueue`
  - Push notifications to `pushNotificationQueue`

### 3. Dead Letter Queue (DLQ) Usage
- Used to handle failed or rejected messages.
- Messages that fail in `queueSamet` are moved to `queueSamet.dlq`.
- **AmqpRejectAndDontRequeueException** prevents the reprocessing of failed messages.
- **Example Scenario:**
  - If a message contains the word "fail," it is sent to the Dead Letter Queue (`queueSamet.dlq`).
  - Messages in the DLQ can be reprocessed or manually inspected.

### 4. Binding Operations
- `queueSamet` is directly bound to the **Direct Exchange**.
- `errorQueue` receives only messages matching the `log.error` pattern.
- `allLogsQueue` processes all messages matching the `log.#` pattern.
- **Fanout Exchange** sends messages to all bound queues.

## Postman Test Scenarios
| Test Scenario | Endpoint | Body |
|---------------|----------|------|
| Direct Message | `POST /api/send/direct` | `{ "message":"Direct Exchange Test fail" }` |
| Topic Message (Error) | `POST /api/send/topic?routingKey=log.error` | `"Error Log Test Message"` |
| Topic Message (Info) | `POST /api/send/topic?routingKey=log.info` | `"Info Log Test Message"` |
| Fanout Message | `POST /api/send/fanout` | `"Fanout Test Message"` |

---
This document provides an overview of the key message queuing concepts implemented in this Spring Boot project using RabbitMQ. The project can be explored further by testing different RabbitMQ scenarios.
