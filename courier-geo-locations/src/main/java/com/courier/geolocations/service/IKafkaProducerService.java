package com.courier.geolocations.service;

import com.courier.geolocations.bean.GeoLocationOfCourier;

public interface IKafkaProducerService {
    void send(GeoLocationOfCourier locationOfCourier);
}

