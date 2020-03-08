package com.courier.geolocations.configuration;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "data")
@Getter
public class FileProperties {

    private List<String> filePaths = new ArrayList<>();

}
