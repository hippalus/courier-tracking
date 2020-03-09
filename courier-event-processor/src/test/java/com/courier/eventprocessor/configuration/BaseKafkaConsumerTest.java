package com.courier.eventprocessor.configuration;

import kafka.server.KafkaServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestPropertySource(
        properties = {"spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
@ContextConfiguration(classes = KafkaTestConfig.class)
@Profile("test")
public class BaseKafkaConsumerTest {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    protected EmbeddedKafkaBroker embeddedKafka;

    @Autowired
    protected KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Autowired
    protected KafkaTemplate<String, String> senderTemplate;

    public void setUp() {
        embeddedKafka.brokerProperty("controlled.shutdown.enable", true);

        for (var messageListenerContainer : kafkaListenerEndpointRegistry.getListenerContainers()) {
            System.err.println(messageListenerContainer.getContainerProperties().toString());
            ContainerTestUtils.waitForAssignment(messageListenerContainer, embeddedKafka.getPartitionsPerTopic());
        }
    }

    @AfterAll
    public void tearDown() {
        for (var messageListenerContainer : kafkaListenerEndpointRegistry.getListenerContainers()) {
            messageListenerContainer.stop();
        }

        embeddedKafka.getKafkaServers().forEach(KafkaServer::shutdown);
        embeddedKafka.getKafkaServers().forEach(KafkaServer::awaitShutdown);
    }
}