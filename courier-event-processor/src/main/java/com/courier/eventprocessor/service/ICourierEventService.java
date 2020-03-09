package com.courier.eventprocessor.service;

import java.io.IOException;

public interface ICourierEventService {
    Double getTotalTravelDistance(String courierId) throws IOException;
}
