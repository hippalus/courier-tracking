package com.courier.eventprocessor.repository;

import com.courier.eventprocessor.model.CourierEventDocument;
import com.courier.eventprocessor.model.Store;
import org.elasticsearch.action.index.IndexResponse;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ICourierEventRepository {

    IndexResponse saveEvent(CourierEventDocument courier) throws IOException;

    Double getTotalTravelDistance(String courierId) throws IOException;

    Map<String, Double> getDistanceToStores(CourierEventDocument courierEvent) throws IOException;

    Duration getTheTimeDifferenceBetweenTheLastRecording(CourierEventDocument courierEvent) throws IOException;

    List<CourierEventDocument> getCourierEvents(String courierId) throws IOException;

    Optional<CourierEventDocument> getLastCourierEvent(String courierId) throws IOException;

    Set<Store> getAllStores() throws IOException;
}
