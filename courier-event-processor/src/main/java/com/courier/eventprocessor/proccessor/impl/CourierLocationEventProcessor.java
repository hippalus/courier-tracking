package com.courier.eventprocessor.proccessor.impl;

import com.courier.eventprocessor.model.CourierEventDocument;
import com.courier.eventprocessor.proccessor.StreamProcessor;
import com.courier.eventprocessor.repository.ICourierEventRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CourierLocationEventProcessor implements StreamProcessor {

    private final Double maxDistance;
    private final Duration maxTime;

    public CourierLocationEventProcessor(Double maxDistance, Duration maxTime) {
        this.maxDistance = maxDistance;
        this.maxTime = maxTime;
    }


    @Override
    public void process(CourierEventDocument event, ICourierEventRepository eventRepository) throws IOException {
        eventRepository.saveEvent(event);
        final var lastCourierEvent = eventRepository.getLastCourierEvent(event.getCourier());
        if (lastCourierEvent.isPresent()) {
            final var doesItMatchTheMaxDistance = eventRepository.getDistanceToStores(event).values()
                    .stream()
                    .anyMatch(dist -> dist <= maxDistance);
            final var timeDiff = eventRepository.getTheTimeDifferenceBetweenTheLastRecording(event);

            final var distanceRuleWithStore = eventRepository.getDistanceToStores(event)
                    .entrySet()
                    .stream()
                    .filter(storeDistEntry -> storeDistEntry.getValue() <= maxDistance)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            if (doesItMatchTheMaxDistance && timeDiff.compareTo(maxTime) >= 0) {
                log.info("Courier {} {} entered the store at a distance of {} meters in {}.", event.getCourier(),
                        distanceRuleWithStore.keySet(), distanceRuleWithStore.values(),event.getEventTime());
            }
        }
    }

}
