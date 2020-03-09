package com.courier.eventprocessor.proccessor;

import com.courier.eventprocessor.model.CourierEventDocument;
import com.courier.eventprocessor.repository.ICourierEventRepository;

import java.io.IOException;

public interface StreamProcessor extends AutoCloseable {

    void process(CourierEventDocument event, ICourierEventRepository target) throws IOException;

    @Override
    default void close() {
    }
}
