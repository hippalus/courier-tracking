package com.courier.geolocations.generator;

import java.util.Objects;

import static com.courier.geolocations.generator.Constants.DISTANCE_FACTOR_METERS;

public interface CheckDistance {

    default boolean isTheLocation100MetersFromTheTarget(LocationPoint source) {
       boolean value=false;
        if (Objects.nonNull(source)) {
            value= source.distanceFrom(getTargetPoint()) < DISTANCE_FACTOR_METERS;
        }
        return value;
    }
    LocationPoint getTargetPoint();

}
