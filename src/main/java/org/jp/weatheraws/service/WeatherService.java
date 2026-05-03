package org.jp.weatheraws.service;

import lombok.RequiredArgsConstructor;
import org.jp.weatheraws.client.GeocodeClient;
import org.jp.weatheraws.client.WeatherClient;
import org.jp.weatheraws.dto.geocode.CityDataDto;
import org.jp.weatheraws.dto.geocode.GeoCodingResponseDto;
import org.jp.weatheraws.dto.openmeteo.CurrentDto;
import org.jp.weatheraws.dto.openmeteo.OpenMeteoResponseDto;
import org.jp.weatheraws.model.City;
import org.jp.weatheraws.model.CoordinatesWGS84;
import org.jp.weatheraws.model.TemperatureCategory;
import org.jp.weatheraws.model.WeatherResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherClient weatherClient;
    private final GeocodeClient geocodeClient;

    public WeatherResponse getWeatherForCity(City city) {
        GeoCodingResponseDto geoResponse = geocodeClient.fetchCoordinates(city);
        CityDataDto cityDto = geoResponse.results().getFirst();
        CoordinatesWGS84 coordinates = new CoordinatesWGS84(cityDto.latitude(), cityDto.longitude());

        OpenMeteoResponseDto openMeteoResponseDto = weatherClient.fetchCurrentTemperature(coordinates);
        CurrentDto currentDto = openMeteoResponseDto.current();

        return new WeatherResponse(
                city.name(),
                currentDto.temperature(),
                currentDto.time(),
                TemperatureCategory.from(currentDto.temperature())
        );
    }
}
