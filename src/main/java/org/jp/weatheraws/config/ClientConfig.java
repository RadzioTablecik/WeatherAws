package org.jp.weatheraws.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientConfig {
    @Value("${clients.weather.openmeteo}")
    private String openMeteoUrl;

    @Bean
    public RestClient weatherRestClient() {
        return RestClient.builder()
                .baseUrl(openMeteoUrl)
                .build();
    }
//
//    @Bean
//    public RestClient geocodingClient() {
//        return RestClient.create();
//    }
}
