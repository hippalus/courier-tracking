package com.courier.geolocations.filereader;

import com.courier.geolocations.bean.GeoLocationOfCourier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Paths;

import static java.lang.System.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CsvFileReaderTest {

    @Autowired
    private CsvFileReader csvFileReader;

    @Test
    void context_load() {
        assertNotNull(csvFileReader);
    }


    @Test
    void bean_should_be_build_when_the_file_path_and_class_are_given() throws Exception {
        //given:
        var csvFileUri = ClassLoader.getSystemResource("test.csv").toURI();

        //when:
        final var courierList = csvFileReader.beanBuilderWithColumnPosition(Paths.get(csvFileUri), GeoLocationOfCourier.class);

        //then:
        assertNotNull(courierList);
        assertEquals(10, courierList.size());
        courierList.forEach(out::println);
    }
}
