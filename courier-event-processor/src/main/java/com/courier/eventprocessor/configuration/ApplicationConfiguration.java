package com.courier.eventprocessor.configuration;

import com.courier.eventprocessor.properties.ConfigProperties;
import lombok.AllArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
@ComponentScan
public class ApplicationConfiguration {
    private final ConfigProperties properties;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost(properties.getClients().getHostname()
                , properties.getClients().getHttpPort(), properties.getClients().getScheme())));
    }
}