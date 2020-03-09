package com.courier.eventprocessor.repository;

import com.courier.eventprocessor.model.CourierEventDocument;
import com.courier.eventprocessor.model.Store;
import com.courier.eventprocessor.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    /*{
        "query": {
            "bool": {
                "must": [
                    { "term": { "courier": "004" } },

                ]
            }
        },
        "aggs": {
            "make_line": {
                "geo_line": {
                    "geo_point": {"field": "location"},
                    "sort": { "field": "timestamp" }
                }
            }
        }
    }
    */
    @Override
    public Double getTotalTravelDistance(String courierId) throws IOException {

        return null;
    }

    @Override
    public Map<String, Double> getDistanceToStores(CourierEventDocument courierEvent) throws IOException {

        return null;
    }


    @Override
    public Duration getTheTimeDifferenceBetweenTheLastRecording(CourierEventDocument courierEvent) throws IOException {
        return null;
    }

    @Override
    public List<CourierEventDocument> getCourierEvents(String courierId) throws IOException {

        final var searchSourceBuilder = getSearchSourceBuilder().query(QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("courier", courierId)))
                .aggregation(AggregationBuilders.terms("geo_point").field("location"))
                .sort("eventTime", SortOrder.DESC);

        final var source = getSearchRequest(EVENT_INDEX_NAME).source(searchSourceBuilder);

        var searchResponse = client.search(source, RequestOptions.DEFAULT);
        var terms = searchResponse.getAggregations().get("courier");

        return null;
    }

    @Override
    public Set<Store> getAllStores() throws IOException {
        return null;
    }

    private SearchSourceBuilder getSearchSourceBuilder() {
        return new SearchSourceBuilder();
    }

    private SearchRequest getSearchRequest(String indexName) {
        return new SearchRequest(indexName);
    }
}
