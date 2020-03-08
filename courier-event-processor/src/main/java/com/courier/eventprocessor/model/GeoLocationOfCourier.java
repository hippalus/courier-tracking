package com.courier.eventprocessor.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class GeoLocationOfCourier {
    private Instant eventTime;
    private String courier;
    private Double latitude;
    private Double longitude;
}
