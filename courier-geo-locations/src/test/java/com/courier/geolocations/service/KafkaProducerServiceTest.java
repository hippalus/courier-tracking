package com.courier.geolocations.service;

import com.courier.geolocations.TestUtils;
import com.courier.geolocations.bean.GeoLocationOfCourier;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.Map;

import static com.courier.geolocations.TestUtils.TOPIC_NAME;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.kafka.test.utils.KafkaTestUtils.getSingleRecord;

@DirtiesContext
@Slf4j
@EmbeddedKafka(
        partitions = 1,
        topics = {"GeoLocationOfCourier"
        }
)
@SpringBootTest(
        properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        classes = {
                KafkaProducerService.class,
                KafkaAutoConfiguration.class,
        }
)
@ExtendWith(SpringExtension.class)
class KafkaProducerServiceTest {

    @Autowired
    private IKafkaProducerService producerService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Test
    void contextLoad() {
        assertThat(producerService).isNotNull();
    }

    @Test
    void it_should_send_geo_location_of_courier_event() throws InterruptedException, JsonProcessingException {
        //given:
        var courierLocationEvent = GeoLocationOfCourier.builder()
                .eventTime(Instant.now())
                .courier("TestCourier")
                .latitude(41.0558328298673)
                .longitude(29.0238672494888)
                .build();
        var json = TestUtils.getJsonString(courierLocationEvent);

        //when:
        producerService.send(courierLocationEvent);

        //then:
        final Consumer<String, String> consumer = TestUtils.buildConsumer(StringDeserializer.class,
                StringDeserializer.class, embeddedKafka);

        embeddedKafka.consumeFromEmbeddedTopics(consumer, TOPIC_NAME);
        final ConsumerRecord<String, String> record = getSingleRecord(consumer, TOPIC_NAME);

        assertThat(record.value().equals(json)).isTrue();
        assertThat(record.key()).isNullOrEmpty();
    }

}
