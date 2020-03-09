package com.courier.eventprocessor.repository;

import com.courier.eventprocessor.model.CourierEventDocument;
import com.courier.eventprocessor.model.GeoPoint;
import com.courier.eventprocessor.model.Store;
import com.courier.eventprocessor.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Component
public class CourierEventRepository implements ICourierEventRepository {
    private static final String EVENT_INDEX_NAME = "courier-location-event";
    private static final String STORES_INDEX_NAME = "stores";
    private static final String COURIER = "courier";
    private static final String EVENT_TIME = "eventTime";

    private final RestHighLevelClient client;

    @Override
    public void saveEvent(CourierEventDocument courier) throws IOException {
        var indexRequest = new IndexRequest(EVENT_INDEX_NAME);
        final var jsonString = Utils.getJsonString(courier);
        indexRequest.source(jsonString, XContentType.JSON);
         client.index(indexRequest, RequestOptions.DEFAULT);
    }

    @Override
    public Double getTotalTravelDistance(String courierId) throws IOException {
        var courierEventDocuments = getCourierEvents(courierId);
        double dist = 0.0;
        if (!CollectionUtils.isEmpty(courierEventDocuments)) {
            for (int i = 0; i < courierEventDocuments.size() - 1; i++) {
                GeoPoint prevPoint = courierEventDocuments.get(i).getLocation();
                GeoPoint nextPoint = courierEventDocuments.get(i + 1).getLocation();
                dist += GeoDistance.PLANE.calculate(prevPoint.getLat(), prevPoint.getLon(),
                        nextPoint.getLat(), nextPoint.getLon(),
                        DistanceUnit.METERS);
            }
        }
        return dist;
    }

    @Override
    public Map<String, Double> getDistanceToStores(CourierEventDocument courierEvent) throws IOException {
        Map<String, Double> storeDistances = new HashMap<>();
        Set<Store> stores = getAllStores();
        stores.stream()
                .forEach(store -> {
                    double distance = GeoDistance.PLANE.calculate(courierEvent.getLocation().getLat(),
                            courierEvent.getLocation().getLon(),
                            store.getLocation().getLat(),
                            store.getLocation().getLon(),
                            DistanceUnit.METERS);
                    storeDistances.put(store.getName(), distance);
                });
        return storeDistances;
    }

    @Override
    public Duration getTheTimeDifferenceBetweenTheLastRecording(CourierEventDocument courierEvent) throws IOException {
        final var searchSourceBuilder = getSearchSourceBuilder()
                .size(1)
                .query(QueryBuilders.boolQuery().must(QueryBuilders.termQuery(COURIER, courierEvent.getCourier())))
                .sort(EVENT_TIME, SortOrder.DESC);

        final var source = getSearchRequest(EVENT_INDEX_NAME).source(searchSourceBuilder);

        var searchResponse = client.search(source, RequestOptions.DEFAULT);
        Duration duration = null;
        if (searchResponse.getHits().getTotalHits().value > 0) {
            SearchHit searchHit = searchResponse.getHits().getHits()[0];
            var lastRecordedCourierEvent = Utils.jsonToPojo(searchHit.getSourceAsString(),
                    CourierEventDocument.class);
            if (lastRecordedCourierEvent != null) {
                final var last = lastRecordedCourierEvent.getEventTime();
                final var current = courierEvent.getEventTime();
                duration = Duration.between(last, current);
            }
        }

        return duration;
    }

    @Override
    public List<CourierEventDocument> getCourierEvents(String courierId) throws IOException {
        final var searchHitsSizeSourceBuilder = getSearchSourceBuilder()
                .query(QueryBuilders.boolQuery().must(QueryBuilders.termQuery(COURIER, courierId)));

        final var hitsSource = getSearchRequest(EVENT_INDEX_NAME).source(searchHitsSizeSourceBuilder);

        var hitsSearchResponse = client.search(hitsSource, RequestOptions.DEFAULT);
        var totalHitsSize = hitsSearchResponse.getHits().getTotalHits();

        final var searchSourceBuilder = getSearchSourceBuilder()
                .size((int) totalHitsSize.value)
                .query(QueryBuilders.boolQuery().must(QueryBuilders.termQuery(COURIER, courierId)))
                .sort(EVENT_TIME, SortOrder.DESC);

        final var source = getSearchRequest(EVENT_INDEX_NAME).source(searchSourceBuilder);

        var searchResponse = client.search(source, RequestOptions.DEFAULT);
        var totalHits = searchResponse.getHits().getHits();

        return Arrays.stream(totalHits)
                .map(searchHit -> {
                    try {
                        return Utils.jsonToPojo(searchHit.getSourceAsString(), CourierEventDocument.class);
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    @Override
    public Set<Store> getAllStores() throws IOException {
        final var searchSourceBuilder = getSearchSourceBuilder().size(100).query(QueryBuilders.matchAllQuery());
        final var source = getSearchRequest(STORES_INDEX_NAME).source(searchSourceBuilder);

        var searchResponse = client.search(source, RequestOptions.DEFAULT);
        var totalHits = searchResponse.getHits().getHits();

        return Arrays.stream(totalHits)
                .map(searchHit -> {
                    try {
                        return Utils.jsonToPojo(searchHit.getSourceAsString(), Store.class);
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<CourierEventDocument> getLastCourierEvent(String courierId) throws IOException {
        final var searchSourceBuilder = getSearchSourceBuilder()
                .size(1)
                .query(QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery(COURIER, courierId)))
                .sort(EVENT_TIME, SortOrder.DESC);

        final var source = getSearchRequest(EVENT_INDEX_NAME).source(searchSourceBuilder);

        var searchResponse = client.search(source, RequestOptions.DEFAULT);
        CourierEventDocument lastRecordedCourierEvent = null;
        if (searchResponse.getHits().getTotalHits().value > 0) {
            SearchHit searchHit = searchResponse.getHits().getHits()[0];
            lastRecordedCourierEvent = Utils.jsonToPojo(searchHit.getSourceAsString(), CourierEventDocument.class);
        }
        return Optional.ofNullable(lastRecordedCourierEvent);
    }

    private SearchSourceBuilder getSearchSourceBuilder() {
        return new SearchSourceBuilder();
    }

    private SearchRequest getSearchRequest(String indexName) {
        return new SearchRequest(indexName);
    }
}
