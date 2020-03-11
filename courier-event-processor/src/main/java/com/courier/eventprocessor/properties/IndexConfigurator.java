package com.courier.eventprocessor.properties;

import com.courier.eventprocessor.model.GeoPoint;
import com.courier.eventprocessor.model.Store;
import com.courier.eventprocessor.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private static final String PROPERTIES = "properties";
    private static final String LOCATION = "location";
    private static final String INDEX_NUMBER_OF_SHARDS = "index.number_of_shards";
    private static final String INDEX_NUMBER_OF_REPLICAS = "index.number_of_replicas";

    private final RestHighLevelClient client;
    private final ConfigProperties configProperties;

    @PostConstruct
    private void createIndexWithMapping() {
        final var indicesClient = client.indices();
        var storesRequest = new GetIndexRequest(configProperties.getIndex().get(STORES_YML_INDEX).getName());
        var courierRequest = new GetIndexRequest(configProperties.getIndex().get(COURIER_YML_INDEX).getName());
        try {
            final var storesExists = indicesClient.exists(storesRequest, DEFAULT);
            final var courierExist = indicesClient.exists(courierRequest, DEFAULT);
            if (!storesExists) {
                var createIndexRequest = storesIndex();
                indicesClient.create(createIndexRequest, DEFAULT);
                log.info("Index {} created", configProperties.getIndex().get(STORES_YML_INDEX).getName());
            }
            bulkStores(STORE_LIST);
            log.info("Default stores added {}",STORE_LIST.toString());
            if (!courierExist) {
                var createIndexRequest = getCourierIndex();
                indicesClient.create(createIndexRequest, DEFAULT);
                log.info("Index {} created", configProperties.getIndex().get(COURIER_YML_INDEX).getName());
            }
        } catch (IOException e) {
            log.error(String.format("The exception was thrown in createIndexWithMapping method.%s", e));
        }

    }


    private CreateIndexRequest getCourierIndex() throws IOException {
        var createIndexRequest = new CreateIndexRequest(configProperties.getIndex()
                .get(COURIER_YML_INDEX)
                .getName());

        createIndexRequest.settings(Settings.builder()
                .put(INDEX_NUMBER_OF_SHARDS, configProperties.getIndex()
                        .get(COURIER_YML_INDEX).getShard())
                .put(INDEX_NUMBER_OF_REPLICAS, configProperties.getIndex()
                        .get(COURIER_YML_INDEX).getReplica())
                .build());

        var contentBuilder = XContentFactory.jsonBuilder()
                .startObject()
                .startObject(PROPERTIES)
                .startObject("eventTime")
                .field(TYPE, DATE)
                .endObject()
                .startObject("courier")
                .field(TYPE, KEYWORD)
                .endObject()
                .startObject(LOCATION)
                .field(TYPE, GEO_POINT)
                .endObject()
                .endObject()
                .endObject();
        createIndexRequest.mapping(contentBuilder);
        return createIndexRequest;
    }

    private CreateIndexRequest storesIndex() throws IOException {
        var createIndexRequest = new CreateIndexRequest(configProperties.getIndex()
                .get(STORES_YML_INDEX)
                .getName());

        createIndexRequest.settings(Settings.builder()
                .put(INDEX_NUMBER_OF_SHARDS, configProperties.getIndex()
                        .get(STORES_YML_INDEX).getShard())
                .put(INDEX_NUMBER_OF_REPLICAS, configProperties.getIndex()
                        .get(STORES_YML_INDEX).getReplica())
                .build());

        var contentBuilder = XContentFactory.jsonBuilder()
                .startObject()
                .startObject(PROPERTIES)
                .startObject("name")
                .field(TYPE, KEYWORD)
                .endObject()
                .startObject(LOCATION)
                .field(TYPE, GEO_POINT)
                .endObject()
                .endObject()
                .endObject();
        createIndexRequest.mapping(contentBuilder);
        return createIndexRequest;
    }

    private void bulkStores(List<Store> storeList) throws IOException {
        var bulkRequest = new BulkRequest();
        storeList.forEach(store -> {
            var indexRequest = new IndexRequest(configProperties.getIndex().get(STORES_YML_INDEX).getName());
            try {
                indexRequest.source(Utils.getJsonString(store), XContentType.JSON);
            } catch (JsonProcessingException e) {
                log.error(e.toString());
            }
            bulkRequest.add(indexRequest);
        });
        final var responses = client.bulk(bulkRequest, DEFAULT);
        if (responses.hasFailures()) {
            bulkStores(storeList);
        }
    }

    private static List<Store> STORE_LIST = new ArrayList<>();

    static {
        STORE_LIST.add(Store.builder()
                .name("Ataşehir MMM Migros")
                .location(GeoPoint.builder()
                        .lat(40.9923307)
                        .lon(29.1244229)
                        .build())
                .build());
        STORE_LIST.add(Store.builder()
                .name("Novada MMM Migros")
                .location(GeoPoint.builder()
                        .lat(40.986106)
                        .lon(29.1161293)
                        .build())
                .build());
        STORE_LIST.add(Store.builder()
                .name("Beylikdüzü 5M Migros")
                .location(GeoPoint.builder()
                        .lat(41.0066851)
                        .lon(28.6552262)
                        .build())
                .build());
        STORE_LIST.add(Store.builder()
                .name("Ortaköy MMM Migros")
                .location(GeoPoint.builder()
                        .lat(41.055783)
                        .lon(29.0210292)
                        .build())
                .build());
        STORE_LIST.add(Store.builder()
                .name("Caddebostan MMM Migros")
                .location(GeoPoint.builder()
                        .lat(40.9632463)
                        .lon(29.0630908)
                        .build())
                .build());

    }


}
