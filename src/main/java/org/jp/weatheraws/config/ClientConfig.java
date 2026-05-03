package org.jp.weatheraws.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class ClientConfig {
    @Value("${clients.weather.openmeteo}")
    private String openMeteoUrl;

    @Bean
    public RestClient weatherRestClient() {
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(Duration.ofSeconds(5));
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
