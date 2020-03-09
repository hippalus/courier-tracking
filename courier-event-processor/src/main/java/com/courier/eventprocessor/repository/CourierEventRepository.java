package com.courier.eventprocessor.repository;

import com.courier.eventprocessor.model.CourierEventDocument;
import com.courier.eventprocessor.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@AllArgsConstructor
@Slf4j
@Component
public class CourierEventRepository implements ICourierEventRepository {
    private static final String EVENT_INDEX_NAME = "courier-location-event";
    private final RestHighLevelClient client;

    @Override
    public IndexResponse saveEvent(CourierEventDocument courier) throws IOException {
        var indexRequest = new IndexRequest(EVENT_INDEX_NAME);
        final var jsonString = Utils.getJsonString(courier);
        indexRequest.source(jsonString, XContentType.JSON);
        return client.index(indexRequest, RequestOptions.DEFAULT);
    }
}
