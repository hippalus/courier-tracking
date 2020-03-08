package com.courier.eventprocessor.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService implements IKafkaConsumerService {

    @Override
    @KafkaListener(topics="${kafka.topic}")
    public void consume(@Payload String message) {
        log.info(message);

    }
}
