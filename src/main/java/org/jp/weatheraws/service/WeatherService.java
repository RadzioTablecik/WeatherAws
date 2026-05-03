package org.jp.weatheraws.service;

import lombok.RequiredArgsConstructor;
import org.jp.weatheraws.client.WeatherClient;
import org.jp.weatheraws.dto.openmeteo.CurrentDto;
import org.jp.weatheraws.dto.openmeteo.OpenMeteoResponseDto;
import org.jp.weatheraws.model.CoordinatesWGS84;
import org.jp.weatheraws.model.TemperatureCategory;
import org.jp.weatheraws.model.WeatherResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherClient weatherClient;

    public WeatherResponse getCurrentTemperature(CoordinatesWGS84 coordinates) {
        OpenMeteoResponseDto openMeteoResponseDto = weatherClient.fetchCurrentTemperature(coordinates);
        CurrentDto currentDto = openMeteoResponseDto.current();

        return new WeatherResponse(
                currentDto.temperature(),
                currentDto.time(),
                TemperatureCategory.from(currentDto.temperature())
        );
    }
}
