package com.courier.eventprocessor.service;

import com.courier.eventprocessor.repository.ICourierEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class CourierEventService implements ICourierEventService {
    private final ICourierEventRepository eventRepository;
    @Override
    public Double getTotalTravelDistance(String courierId) throws IOException {
        return eventRepository.getTotalTravelDistance(courierId);
    }
}
