package org.jp.weatheraws;

import org.jp.weatheraws.dto.aws.WeatherRequest;
import org.jp.weatheraws.model.City;
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
    public Function<WeatherRequest, WeatherResponse> getWroclawTemperature(WeatherService weatherService) {
        return input -> {
            City city = new City(input.city());

            return weatherService.getWeatherForCity(city);
        };
    }

}
