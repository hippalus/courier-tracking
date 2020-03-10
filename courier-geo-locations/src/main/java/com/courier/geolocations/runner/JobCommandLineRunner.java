package com.courier.geolocations.runner;

import com.courier.geolocations.generator.ICourierGenerator;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("!test")
@AllArgsConstructor
public class JobCommandLineRunner implements CommandLineRunner {
    private final ICourierGenerator courierGenerator;

    @Override
    public void run(String... args) throws Exception {
        boolean shouldIRun = true;
        while (shouldIRun) {
            try {
                courierGenerator.generateAndSendEvent();
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                shouldIRun = false;
                Thread.currentThread().interrupt();
            }

        }
    }
}
