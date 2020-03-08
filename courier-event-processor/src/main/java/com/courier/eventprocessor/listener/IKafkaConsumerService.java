package com.courier.eventprocessor.listener;

import org.springframework.messaging.handler.annotation.Payload;

public interface IKafkaConsumerService {
    void consume(@Payload String message);
}
