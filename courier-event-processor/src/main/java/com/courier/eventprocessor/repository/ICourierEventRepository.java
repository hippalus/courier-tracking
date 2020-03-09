package com.courier.eventprocessor.repository;

import com.courier.eventprocessor.model.CourierEventDocument;
import org.elasticsearch.action.index.IndexResponse;

import java.io.IOException;

public interface ICourierEventRepository {

    IndexResponse saveEvent(CourierEventDocument courier) throws IOException;
}
