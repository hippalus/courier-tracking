package com.courier.eventprocessor.proccessor.impl;

import com.courier.eventprocessor.proccessor.EventProcessorFactory;
import com.courier.eventprocessor.proccessor.StreamProcessor;
import com.google.auto.service.AutoService;

import java.time.Duration;

@AutoService(EventProcessorFactory.class)
public final class CourierEventProcessorFactory implements EventProcessorFactory {
    @Override
    public StreamProcessor createProcessor(Double maxDistance, Duration maxTime) {
        return new CourierLocationEventProcessor(maxDistance,maxTime);
    }
}
