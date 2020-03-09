package com.courier.eventprocessor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class CourierEventDocument {
    private Instant eventTime;
    private String courier;
    private GeoPoint location;

}
