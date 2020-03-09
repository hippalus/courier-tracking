package com.courier.geolocations.generator;

import com.courier.geolocations.bean.GeoLocationOfCourier;

import java.time.Duration;

public interface ICourierGenerator {
    void generateAndSendEvent();

    void IfTheLocationIs100MetersToTheTargetWait1MinuteElseRandom(GeoLocationOfCourier prevEvent,
                                                                  GeoLocationOfCourier currentEvent);

    boolean isTheLocation100MetersFromTheTarget(GeoLocationOfCourier courier);

    void addEventTime(GeoLocationOfCourier prevEvent, GeoLocationOfCourier currentEvent, Duration duration);
}
