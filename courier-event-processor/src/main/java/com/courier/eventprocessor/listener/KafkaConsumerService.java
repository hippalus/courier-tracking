package com.courier.eventprocessor.listener;

import com.courier.eventprocessor.model.CourierEvent;
import com.courier.eventprocessor.proccessor.EventProcessorFactory;
import com.courier.eventprocessor.proccessor.StreamProcessor;
import com.courier.eventprocessor.repository.ICourierEventRepository;
import com.courier.eventprocessor.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ServiceLoader;

@Service
@Slf4j
public class KafkaConsumerService implements IKafkaConsumerService {


    private ICourierEventRepository courierEventRepository;

    @Autowired
    public void setCourierEventRepository(ICourierEventRepository courierEventRepository) {
        this.courierEventRepository = courierEventRepository;
    }

    @Override
    @KafkaListener(topics = "${kafka.topic}")
    public void consume(@Payload String message) throws IOException {
        var factories = ServiceLoader.load(EventProcessorFactory.class).iterator();
        if (!factories.hasNext()) {
            throw new IllegalStateException("No EventProcessorFactory found");
        }
        var geoLocationOfCourier = Utils.jsonToPojo(message, CourierEvent.class);

        try (EventProcessorFactory factory = factories.next();
             StreamProcessor processor = factory.createProcessor(100.0, Duration.of(1, ChronoUnit.MINUTES))) {
            processor.process(geoLocationOfCourier.toRecordableEvent(), courierEventRepository);
        }
    }

}
