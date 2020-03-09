package com.courier.eventprocessor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourierEvent {
    private Instant eventTime;
    private String courier;
    private Double latitude;
    private Double longitude;


    public CourierEventDocument toRecordableEvent() {
        checkArguments();
        return CourierEventDocument.builder()
                .courier(courier)
                .eventTime(eventTime)
                .location(GeoPoint.builder()
                        .lat(latitude)
                        .lon(longitude)
                        .build())
                .build();
    }

    private void checkArguments() {
        if (Objects.isNull(eventTime)) {
            throw new IllegalArgumentException("Event Time must not be null !");
        }
        if (Objects.isNull(courier)) {
            throw new IllegalArgumentException("Courier information must not be null !");
        }
        if (Objects.isNull(latitude)) {
            throw new IllegalArgumentException("Latitude must not be null !");
        }
        if (Objects.isNull(longitude)) {
            throw new IllegalArgumentException("Longitude must not be null !");
        }
    }

}
