package com.courier.geolocations.bean;

import com.opencsv.bean.CsvBindByPosition;
import lombok.*;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Builder
public class GeoLocationOfCourier extends CsvBean {

    private Instant eventTime;
    @CsvBindByPosition(position = 0)
    private String courier;
    @CsvBindByPosition(position = 1)
    private Double latitude;
    @CsvBindByPosition(position = 2)
    private Double longitude;


}
