package com.courier.eventprocessor.proccessor.impl;

import com.courier.eventprocessor.model.CourierEventDocument;
import com.courier.eventprocessor.proccessor.StreamProcessor;
import com.courier.eventprocessor.repository.ICourierEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

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

        final var lastCourierEvent = eventRepository.getLastCourierEvent(event.getCourier());
        if (lastCourierEvent.isPresent()) {
            final var distanceRuleWithStore = eventRepository.getDistanceToStores(event)
                    .entrySet()
                    .stream()
                    .filter(storeDistEntry -> storeDistEntry.getValue() <= maxDistance)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            if (!CollectionUtils.isEmpty(distanceRuleWithStore) &&
                    isTheEventTimeDifferenceGreaterThanTheMaximumTime(event, lastCourierEvent.get())) {
                log.info("Courier {} {} entered the store at a distance of {} meters in {}.",
                        event.getCourier(),distanceRuleWithStore.keySet(),
                        distanceRuleWithStore.values(), event.getEventTime());
            }
        }
        eventRepository.saveEvent(event);

    }

    private boolean isTheEventTimeDifferenceGreaterThanTheMaximumTime(CourierEventDocument current, CourierEventDocument prev) {
        final var between = Duration.between(current.getEventTime(), prev.getEventTime());
        return between.compareTo(maxTime)>0;
    }

}
