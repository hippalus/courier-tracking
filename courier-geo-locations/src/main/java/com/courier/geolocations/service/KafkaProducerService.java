package com.courier.geolocations.service;

import com.courier.geolocations.bean.GeoLocationOfCourier;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.courier.geolocations.utils.JsonUtils.pojoToJson;

@Service
@AllArgsConstructor
@Slf4j
public class KafkaProducerService implements IKafkaProducerService {
    private static final String TOPIC_NAME = "GeoLocationOfCourier";

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void send(GeoLocationOfCourier locationOfCourier) {
        final var jsonStringEvent = pojoToJson(locationOfCourier);
        if (Objects.nonNull(jsonStringEvent)) {
            kafkaTemplate.send(TOPIC_NAME, jsonStringEvent);
        }
    }
}
