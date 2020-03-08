package com.courier.eventprocessor.configuration;

import com.courier.eventprocessor.listener.IKafkaConsumerService;
import com.courier.eventprocessor.listener.KafkaConsumerService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;

@TestConfiguration
@Profile("kafka_test")
@EnableKafka
public class KafkaBeansConfig {

    @Bean
    public IKafkaConsumerService consumerService() {
        return new KafkaConsumerService();
    }


}
