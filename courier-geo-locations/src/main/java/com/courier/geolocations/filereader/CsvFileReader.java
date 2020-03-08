package com.courier.geolocations.filereader;

import com.courier.geolocations.bean.CsvBean;
import com.courier.geolocations.bean.GeoLocationOfCourier;
import com.courier.geolocations.configuration.FileProperties;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.MappingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class CsvFileReader {
    private final FileProperties properties;

    public List<List<CsvBean>> getAllData() {
        return properties.getFilePaths().stream()
                .map(file -> beanBuilderWithColumnPosition(Path.of(file), GeoLocationOfCourier.class))
                .collect(Collectors.toList());

    }

    @SuppressWarnings("rawtypes")
    public List<CsvBean> beanBuilderWithColumnPosition(Path path, Class clazz) {
        ColumnPositionMappingStrategy ms = new ColumnPositionMappingStrategy();
        return beanBuilder(path, clazz, ms);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private List<CsvBean> beanBuilder(Path path, Class clazz, MappingStrategy ms) {
        List<CsvBean> beanList = null;
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            ms.setType(clazz);
            final CsvToBean csvToBean = new CsvToBeanBuilder(reader)
                    .withType(GeoLocationOfCourier.class)
                    .withOrderedResults(true)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withMappingStrategy(ms)
                    .build();
            //Reads all CSV contents into memory (Not suitable for large CSV files)
            beanList = csvToBean.parse();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return beanList;
    }
}
