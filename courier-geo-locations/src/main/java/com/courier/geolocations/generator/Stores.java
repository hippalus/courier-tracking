package com.courier.geolocations.generator;

import java.util.Arrays;
import java.util.List;

public enum Stores implements CheckDistance {

    ATASEHIR_MMM_MIGROS(Constants.ATASEHIR_LOCATION),

    NOVADA_MMM_MIGROS(Constants.NOVADA_LOCATION),

    BEYLIKDUZU_5M_MIGROS(Constants.BEYLIKDUZU_LOCATION),

    ORTAKOY_MMM_MIGROS(Constants.ORTAKOY_LOCATION),

    CADDEBOSTAN_MMM_MIGROS(Constants.CADDEBOSTAN_LOCATION);

    private final LocationPoint locationPoint;

    Stores(LocationPoint locationPoint) {
        this.locationPoint = locationPoint;
    }

    static final List<Stores> ALL_STORES = Arrays.asList(ATASEHIR_MMM_MIGROS, NOVADA_MMM_MIGROS,
            BEYLIKDUZU_5M_MIGROS, ORTAKOY_MMM_MIGROS, CADDEBOSTAN_MMM_MIGROS);

    @Override
    public LocationPoint getTargetPoint() {
        return locationPoint;
    }


}
