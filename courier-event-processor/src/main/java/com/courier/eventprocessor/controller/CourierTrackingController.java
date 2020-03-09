package com.courier.eventprocessor.controller;

import com.courier.eventprocessor.service.ICourierEventService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class CourierTrackingController {
    private final ICourierEventService eventService;

    @PostMapping(value = "/courier/gettotaltraveldistance/")
    public ResponseEntity<Double> getTotalTravelDistance(@RequestParam("courierId") String courierId) throws IOException {
        return ResponseEntity.ok(eventService.getTotalTravelDistance(courierId));
    }

}
