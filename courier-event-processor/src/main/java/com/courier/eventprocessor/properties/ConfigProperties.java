package com.courier.eventprocessor.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application")
public class ConfigProperties {

    private List<Index> index = new ArrayList<>();

    @NestedConfigurationProperty
    private Clients clients = new Clients();


}