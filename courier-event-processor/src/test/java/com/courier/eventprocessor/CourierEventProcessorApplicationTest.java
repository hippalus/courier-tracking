package com.courier.eventprocessor;

import com.courier.eventprocessor.configuration.BaseKafkaConsumerTest;
import com.courier.eventprocessor.configuration.KafkaBeansConfig;
import com.courier.eventprocessor.listener.IKafkaConsumerService;
import com.courier.eventprocessor.model.CourierEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@EmbeddedKafka(topics = "GeoLocationOfCourier")
@Import(KafkaBeansConfig.class)
@Slf4j
@SpringBootTest
public class CourierEventProcessorApplicationTest extends BaseKafkaConsumerTest {
    @Configuration
    @ComponentScan(value = {"com.courier.eventprocessor.listener"})
    static class KafkaLocalTestConfig {
    }

    @Autowired
    private IKafkaConsumerService consumerService;

    @BeforeAll
    public void setUp() {
        super.setUp();
    }

    @Test
    public void should_receive_messages_from_kafka_topic() throws Exception {

        //given
        var courierLocationEvent = CourierEvent.builder()
                .eventTime(Instant.now())
                .courier("TestCourier")
                .latitude(41.0558328298673)
                .longitude(29.0238672494888)
                .build();
        var jsonString = TestUtils.getJsonString(courierLocationEvent);

        //when:
        var future = senderTemplate.send("GeoLocationOfCourier", jsonString);

        Thread.sleep(1000);

        //then:
        future.addCallback(new ListenableFutureCallback<>() {
            @SneakyThrows
            @Override
            public void onSuccess(SendResult<String, String> result) {

                log.info("successfully sent message='{}' with offset={}", jsonString, result.getRecordMetadata().offset());

                Mockito.verify(consumerService, Mockito.times(1))
                        .consume(result.getProducerRecord().value());

                assertThat(result.getProducerRecord().value())
                        .isEqualTo(jsonString);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("unable to send message='{}'", jsonString, ex);
                fail("Failed Test Message Receive");
            }
        });
    }
}
