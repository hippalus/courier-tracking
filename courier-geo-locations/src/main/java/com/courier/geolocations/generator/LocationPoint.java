package com.courier.geolocations.generator;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Value
@Slf4j
public class LocationPoint {

    private static final Double ZERO = 0D;

    private Double latitude;
    private Double longitude;

    private LocationPoint(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static LocationPoint of(Double latitude, Double longitude) {
        return new LocationPoint(latitude, longitude);
    }

    public Double distanceFrom(LocationPoint point) {
        var distance=ZERO;
        if (!point.equals(this)) {
            distance=calculateDistance(this.latitude,this.longitude,point.getLatitude(),point.getLongitude());
        }
        return distance;
    }
    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        var earthRadius = 6371000; //meters
        var dLat = Math.toRadians(lat2-lat1);
        var dLng = Math.toRadians(lng2-lng1);
        var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng/2) * Math.sin(dLng/2);
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return (earthRadius * c) ;
    }

    @Override
    public String toString() {
        return String.format("%s,%s", latitude, longitude);

    }
}
