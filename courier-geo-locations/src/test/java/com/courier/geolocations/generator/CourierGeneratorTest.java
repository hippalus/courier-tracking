package com.courier.geolocations.generator;

import com.courier.geolocations.bean.GeoLocationOfCourier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CourierGeneratorTest {
    private static final Double ATASEHIR_LATITUDE_91_METER = 40.99211927;
    private static final Double ATASEHIR_LONGITUDE_91_METER = 29.12548200;
    private static final Double ATASEHIR_LATITUDE_134_METER = 40.99111927;
    private static final Double ATASEHIR_LONGITUDE_134_METER = 29.12448200;

    @Autowired
    private ICourierGenerator courierGenerator;

    @Test
    void contextLoad() {
        assertThat(courierGenerator).isNotNull();
    }

    @Test
    void radius_rule_of_100_meters() {
        //given:
        var courier = newCourier();

        //when:
        final var isEntrance = courierGenerator.isTheLocation100MetersFromTheTarget(courier);

        //then:
        assertThat(isEntrance).isTrue();

    }
    @Test
    void test_100_Meter_Out_Side() {
        //given:
        var courier = newCourier100MeterOutSide();

        //when:
        final var isEntrance = courierGenerator.isTheLocation100MetersFromTheTarget(courier);

        //then:
        assertThat(isEntrance).isFalse();

    }

    @Test
    void should_be_time_between_2_points_must_be_parametric() {
        //given:
        var courierEvent = newCourier();
        var courierNextEvent = newCourier();

        //when:
        courierGenerator.addEventTime(courierEvent, courierNextEvent, Duration.of(30, ChronoUnit.SECONDS));

        //then:
        assertThat(Duration.between(courierEvent.getEventTime(), courierNextEvent.getEventTime())).hasSeconds(30);
    }


    @Test
    @DisplayName("should be create the same location information for 1 minute in the radius of 100 meters of the target")
    void radius_rule_of_100_meters_for_1_minute() {
        //given:
        var courierEvent = newCourier();
        var courierNextEvent = newCourier();

        //when:
        courierGenerator.IfTheLocationIs100MetersToTheTargetWait1MinuteElseRandom(courierEvent, courierNextEvent);

        //then:
        assertThat(Duration.between(courierEvent.getEventTime(), courierNextEvent.getEventTime())).hasMinutes(1);

    }
    @Test
    void event_time_must_be_random_except_for_the_100_meter_rule() {
        //given:
        var courierEvent = newCourier();
        var courierNextEvent = newCourier100MeterOutSide();

        //when:
        courierGenerator.IfTheLocationIs100MetersToTheTargetWait1MinuteElseRandom(courierEvent, courierNextEvent);

        //then:
        assertThat(courierEvent.getEventTime()).isBefore(courierNextEvent.getEventTime());

    }


    private GeoLocationOfCourier newCourier() {
        return GeoLocationOfCourier.builder()
                .eventTime(Instant.now())
                .courier("test-courier")
                .latitude(ATASEHIR_LATITUDE_91_METER)
                .longitude(ATASEHIR_LONGITUDE_91_METER)
                .build();
    }
    private GeoLocationOfCourier newCourier100MeterOutSide() {
        return GeoLocationOfCourier.builder()
                .eventTime(Instant.now())
                .courier("test-courier")
                .latitude(ATASEHIR_LATITUDE_134_METER)
                .longitude(ATASEHIR_LONGITUDE_134_METER)
                .build();
    }

}