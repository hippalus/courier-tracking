package com.courier.eventprocessor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public  class GeoPoint{
    private Double lat;
    private Double lon;
}