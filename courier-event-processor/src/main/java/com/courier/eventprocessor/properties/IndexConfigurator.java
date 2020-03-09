package com.courier.eventprocessor.properties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static org.elasticsearch.client.RequestOptions.DEFAULT;

@Component
@Slf4j
@RequiredArgsConstructor
public class IndexConfigurator {
    private static final int STORES_YML_INDEX = 0;
    private static final int COURIER_YML_INDEX = 1;
    private static final String KEYWORD = "keyword";
    private static final String TYPE = "type";
    private static final String GEO_POINT = "geo_point";
    private static final String DATE = "date";

    private final RestHighLevelClient client;
    private final ConfigProperties properties;

    @PostConstruct
    private void createIndexWithMapping() {
        final var indicesClient = client.indices();
        var storesRequest = new GetIndexRequest(properties.getIndex().get(STORES_YML_INDEX).getName());
        var courierRequest = new GetIndexRequest(properties.getIndex().get(COURIER_YML_INDEX).getName());
        try {
            final var storesExists = indicesClient.exists(storesRequest, DEFAULT);
            final var courierExist = indicesClient.exists(courierRequest, DEFAULT);
            if (!storesExists) {
                var createIndexRequest = storesIndex();
                indicesClient.create(createIndexRequest, DEFAULT);
                log.info("Index {} created", properties.getIndex().get(STORES_YML_INDEX).getName());
            }
            if (!courierExist) {
                var createIndexRequest = getCourierIndex();
                indicesClient.create(createIndexRequest, DEFAULT);
                log.info("Index {} created", properties.getIndex().get(COURIER_YML_INDEX).getName());
            }
        } catch (IOException e) {
            log.error(String.format("The exception was thrown in createIndexWithMapping method.%s", e));
        }

    }


    private CreateIndexRequest getCourierIndex() throws IOException {
        var createIndexRequest = new CreateIndexRequest(properties.getIndex()
                .get(COURIER_YML_INDEX)
                .getName());

        createIndexRequest.settings(Settings.builder()
                .put("index.number_of_shards", properties.getIndex()
                        .get(COURIER_YML_INDEX).getShard())
                .put("index.number_of_replicas", properties.getIndex()
                        .get(COURIER_YML_INDEX).getReplica())
                .build());

        var contentBuilder = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties")
                .startObject("eventTime")
                .field(TYPE, DATE)
                .endObject()
                .startObject("courier")
                .field(TYPE, KEYWORD)
                .endObject()
                .startObject("location")
                .field(TYPE, GEO_POINT)
                .endObject()
                .endObject()
                .endObject();
        createIndexRequest.mapping(contentBuilder);
        return createIndexRequest;
    }

    private CreateIndexRequest storesIndex() throws IOException {
        var createIndexRequest = new CreateIndexRequest(properties.getIndex()
                .get(STORES_YML_INDEX)
                .getName());

        createIndexRequest.settings(Settings.builder()
                .put("index.number_of_shards", properties.getIndex()
                        .get(STORES_YML_INDEX).getShard())
                .put("index.number_of_replicas", properties.getIndex()
                        .get(STORES_YML_INDEX).getReplica())
                .build());

        var contentBuilder = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties")
                .startObject("name")
                .field(TYPE, KEYWORD)
                .endObject()
                .startObject("location")
                .field(TYPE, GEO_POINT)
                .endObject()
                .endObject()
                .endObject();
        createIndexRequest.mapping(contentBuilder);
        return createIndexRequest;
    }

}
