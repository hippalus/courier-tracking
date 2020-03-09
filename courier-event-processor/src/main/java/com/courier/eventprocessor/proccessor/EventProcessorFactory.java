package com.courier.eventprocessor.proccessor;

import java.time.Duration;

public interface EventProcessorFactory extends AutoCloseable {
    StreamProcessor createProcessor(Double maxDistance, Duration maxTime);

    @Override
    default void close() {
    }
}
