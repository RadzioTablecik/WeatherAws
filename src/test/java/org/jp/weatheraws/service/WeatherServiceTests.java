package org.jp.weatheraws.service;

import org.jp.weatheraws.client.WeatherClient;
import org.jp.weatheraws.dto.openmeteo.CurrentDto;
import org.jp.weatheraws.dto.openmeteo.OpenMeteoResponseDto;
import org.jp.weatheraws.model.CoordinatesWGS84;
import org.jp.weatheraws.model.TemperatureCategory;
import org.jp.weatheraws.model.WeatherResponse;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class WeatherServiceTests {
    @Mock
    private WeatherClient weatherClient;
    @InjectMocks
    private WeatherService weatherService;

    @Test
    public void shouldReturnWeatherResponseWithCorrectCategory() {
        // GIVEN
        CoordinatesWGS84 coords = new CoordinatesWGS84(51.1, 17.0);
        double mockTemp = 25.5;
        String mockTime = "2024-03-20T12:00";

        CurrentDto currentDto = new CurrentDto(mockTemp, mockTime);
        OpenMeteoResponseDto mockResponse = new OpenMeteoResponseDto(
                currentDto
        );

        when(weatherClient.fetchCurrentTemperature(coords)).thenReturn(mockResponse);

        // WHEN
        WeatherResponse result = weatherService.getCurrentTemperature(coords);

        // THEN
        assertThat(result.temperature()).isEqualTo(25.5);
        assertThat(result.time()).isEqualTo(mockTime);
        assertThat(result.temperatureCategory()).isEqualTo(TemperatureCategory.WARM);

        System.out.println(result);
    }
}
