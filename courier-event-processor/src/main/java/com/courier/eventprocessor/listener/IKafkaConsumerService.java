package com.courier.eventprocessor.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;


public interface IKafkaConsumerService {
    void consume(@Payload String message) throws JsonProcessingException, IOException;

}
