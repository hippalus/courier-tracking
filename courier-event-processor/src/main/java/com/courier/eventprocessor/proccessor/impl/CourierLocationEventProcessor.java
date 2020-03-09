package com.courier.eventprocessor.proccessor.impl;

import com.courier.eventprocessor.model.CourierEventDocument;
import com.courier.eventprocessor.proccessor.StreamProcessor;
import com.courier.eventprocessor.repository.ICourierEventRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Duration;

@Slf4j
public class CourierLocationEventProcessor implements StreamProcessor {

    private final Double maxDistance;
    private final Duration maxTime;

    public CourierLocationEventProcessor(Double maxDistance, Duration maxTime) {
        this.maxDistance = maxDistance;
        this.maxTime = maxTime;
    }


    @Override
    public void process(CourierEventDocument event, ICourierEventRepository target) throws IOException {
        log.info(event.toString());
        target.saveEvent(event);
    }

}
