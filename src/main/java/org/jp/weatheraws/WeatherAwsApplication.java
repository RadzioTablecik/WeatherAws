package org.jp.weatheraws;

import org.jp.weatheraws.model.City;
import org.jp.weatheraws.model.WeatherResponse;
import org.jp.weatheraws.service.WeatherService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@SpringBootApplication
public class WeatherAwsApplication {

    static void main(String[] args) {
        SpringApplication.run(WeatherAwsApplication.class, args);
    }

    @Bean
    public Function<Map<String, Object>, WeatherResponse> weatherFunction(WeatherService service) {
        return input -> {
            String cityName = Optional.ofNullable((Map<String, String>) input.get("queryStringParameters"))
                    .map(p -> p.get("city"))
                    .filter(s -> !s.isBlank())
                    .orElseThrow(() -> new IllegalArgumentException("Query parameter 'city' is required"));

            return service.getWeatherForCity(new City(cityName));
        };
    }

}
