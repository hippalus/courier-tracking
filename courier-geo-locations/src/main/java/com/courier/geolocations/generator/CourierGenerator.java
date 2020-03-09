package com.courier.geolocations.generator;

import com.courier.geolocations.bean.CsvBean;
import com.courier.geolocations.bean.GeoLocationOfCourier;
import com.courier.geolocations.filereader.CsvFileReader;
import com.courier.geolocations.service.IKafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class CourierGenerator implements ICourierGenerator {
    private final CsvFileReader fileReader;
    private final IKafkaProducerService producerService;


    @Override
    public void generateAndSendEvent() {
        final var allCourierData = fileReader.getAllData();
        for (List<CsvBean> couriers : allCourierData) {
            for (int i = 1; i < couriers.size(); i++) {
                var prev = (GeoLocationOfCourier) couriers.get(i - 1);
                var current = (GeoLocationOfCourier) couriers.get(i);
                IfTheLocationIs100MetersToTheTargetWait1MinuteElseRandom(prev, current);
                producerService.send(prev);
                producerService.send(current);
            }
        }
    }

    @Override
    public void IfTheLocationIs100MetersToTheTargetWait1MinuteElseRandom(GeoLocationOfCourier prevEvent,
                                                                         GeoLocationOfCourier currentEvent) {
        if (prevEvent.getCourier().equals(currentEvent.getCourier()) &&
                isTheLocation100MetersFromTheTarget(prevEvent) &&
                isTheLocation100MetersFromTheTarget(currentEvent)) {
            addEventTime(prevEvent, currentEvent, Duration.of(1, ChronoUnit.MINUTES));
        } else {
            addEventTime(prevEvent, currentEvent,
                    Duration.of(ThreadLocalRandom.current().nextInt(50, 130), ChronoUnit.SECONDS));
        }
    }

    @Override
    public boolean isTheLocation100MetersFromTheTarget(GeoLocationOfCourier courier) {
        var location = LocationPoint.of(courier.getLatitude(), courier.getLongitude());
        return Stores.ALL_STORES
                .stream()
                .map(stores -> stores.isTheLocation100MetersFromTheTarget(location))
                .findFirst()
                .orElse(false);
    }

    @Override
    public void addEventTime(GeoLocationOfCourier prevEvent, GeoLocationOfCourier currentEvent, Duration duration) {
        var eventTime = prevEvent.getEventTime();
        if (Objects.isNull(prevEvent.getEventTime())) {
            eventTime = Instant.now();
            prevEvent.setEventTime(eventTime);
        }
        currentEvent.setEventTime(eventTime.plus(duration));
    }
}
