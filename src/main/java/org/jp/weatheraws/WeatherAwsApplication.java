package org.jp.weatheraws;

import org.jp.weatheraws.model.CoordinatesWGS84;
import org.jp.weatheraws.model.WeatherResponse;
import org.jp.weatheraws.service.WeatherService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
public class WeatherAwsApplication {

    static void main(String[] args) {
        SpringApplication.run(WeatherAwsApplication.class, args);
    }

    @Bean
    public Function<String, WeatherResponse> getWroclawTemperature(WeatherService weatherService) {
        return input -> {
            CoordinatesWGS84 coordinatesWGS84 = new CoordinatesWGS84(
                    51.1,
                    17.0333
            );

            return weatherService.getCurrentTemperature(coordinatesWGS84);
        };
    }

}
