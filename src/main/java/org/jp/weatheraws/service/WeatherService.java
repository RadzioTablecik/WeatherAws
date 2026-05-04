package org.jp.weatheraws.service;

import lombok.RequiredArgsConstructor;
import org.jp.weatheraws.client.GeocodeClient;
import org.jp.weatheraws.client.WeatherProvider;
import org.jp.weatheraws.dto.geocode.CityDataDto;
import org.jp.weatheraws.dto.geocode.GeoCodingResponseDto;
import org.jp.weatheraws.model.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final List<WeatherProvider> weatherProviders;
    private final GeocodeClient geocodeClient;

    public WeatherResponse getWeatherForCity(City city, String providerName) {
        GeoCodingResponseDto geoResponse = geocodeClient.fetchCoordinates(city);
        CityDataDto cityDto = geoResponse.results().getFirst();
        CoordinatesWGS84 coordinates = new CoordinatesWGS84(cityDto.latitude(), cityDto.longitude());

        WeatherProvider provider = weatherProviders.stream()
                .filter(p -> p.supports(providerName))
                .findFirst()
                .orElse(weatherProviders.getFirst()); // Openmeteo is default provider

        WeatherData weatherData = provider.fetchCurrentTemperature(coordinates);

        return new WeatherResponse(
                city.name(),
                weatherData.temperature(),
                weatherData.time(),
                TemperatureCategory.from(weatherData.temperature()),
                providerName
        );
    }
}
