package com.courier.geolocations.generator;

import com.courier.geolocations.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.courier.geolocations.TestUtils.TOPIC_NAME;
import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DirtiesContext
@EmbeddedKafka(
        partitions = 1,
        topics = {"GeoLocationOfCourier"
        }
)
@SpringBootTest(
        properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"
)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CourierGeneratorIT {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;
    @Autowired
    private CourierGenerator courierGenerator;

    @Test
    void should_generate_courier_event_and_send_to_kafka_topic(){
        //when:
        courierGenerator.generateAndSendEvent();

        //then:
        final Consumer<String, String> consumer = TestUtils.buildConsumer(StringDeserializer.class,
                    StringDeserializer.class,embeddedKafka);
        embeddedKafka.consumeFromEmbeddedTopics(consumer, TOPIC_NAME);
        final ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(consumer,500);

        assertThat(records).isNotNull();
    }



}
