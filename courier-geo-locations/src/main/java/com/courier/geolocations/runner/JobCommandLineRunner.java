package com.courier.geolocations.runner;

import com.courier.geolocations.generator.CourierGenerator;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@AllArgsConstructor
public class JobCommandLineRunner implements CommandLineRunner {
    private final CourierGenerator courierGenerator;
    @Override
    public void run(String... args) throws Exception {
        courierGenerator.generateAndSendEvent();
    }
}
