package org.jp.weatheraws.service;

import org.jp.weatheraws.client.GeocodeClient;
import org.jp.weatheraws.client.WeatherProvider;
import org.jp.weatheraws.dto.geocode.CityDataDto;
import org.jp.weatheraws.dto.geocode.GeoCodingResponseDto;
import org.jp.weatheraws.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class WeatherServiceTests {
    @Mock
    private WeatherProvider openMeteoProvider;

    @Mock
    private WeatherProvider secondaryProvider;

    @Mock
    private GeocodeClient geocodeClient;

    private WeatherService weatherService;

    @BeforeEach
    public void setUp() {
        weatherService = new WeatherService(List.of(openMeteoProvider, secondaryProvider), geocodeClient);
    }

    @Test
    public void shouldReturnWeatherUsingDefaultProvider() {
        // GIVEN
        City city = new City("Wroclaw");
        CoordinatesWGS84 coords = new CoordinatesWGS84(51.1, 17.0);

        when(geocodeClient.fetchCoordinates(city)).thenReturn(createMockGeoResponse(51.1, 17.0));
        when(openMeteoProvider.fetchCurrentTemperature(coords)).thenReturn(new WeatherData(25.0, "2026-05-04T12:00"));

        // WHEN
        WeatherResponse response = weatherService.getWeatherForCity(city, null);

        // THEN
        assertThat(response.city()).isEqualTo("Wroclaw");
        assertThat(response.temperature()).isEqualTo(25.0);
        assertThat(response.temperatureCategory()).isEqualTo(TemperatureCategory.WARM);

        verify(openMeteoProvider).fetchCurrentTemperature(any());
        verify(secondaryProvider, never()).fetchCurrentTemperature(any());
    }

    @Test
    public void shouldSelectSpecificProvider() {
        // GIVEN
        City city = new City("Berlin");
        when(geocodeClient.fetchCoordinates(city)).thenReturn(createMockGeoResponse(52.5, 13.4));

        when(secondaryProvider.supports("accuweather")).thenReturn(true);
        when(secondaryProvider.fetchCurrentTemperature(any())).thenReturn(new WeatherData(10.0, "2026-05-04T12:00"));

        // WHEN
        weatherService.getWeatherForCity(city, "accuweather");

        // THEN
        verify(secondaryProvider).fetchCurrentTemperature(any());
        verify(openMeteoProvider, never()).fetchCurrentTemperature(any());
    }

    // Helper function for mocking geolocation
    private GeoCodingResponseDto createMockGeoResponse(double lat, double lon) {
        return new GeoCodingResponseDto(List.of(new CityDataDto(lat, lon)));
    }
}
