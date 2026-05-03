package org.jp.weatheraws.client;

import lombok.extern.slf4j.Slf4j;
import org.jp.weatheraws.dto.openmeteo.OpenMeteoResponseDto;
import org.jp.weatheraws.model.CoordinatesWGS84;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class WeatherClient {
    private final RestClient restClient;

    public WeatherClient(@Qualifier("weatherRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Fetches current weather data from Open-Meteo API.
     * Sample request:
     * <a href="https://api.open-meteo.com/v1/forecast?latitude=51.1&longitude=17.0333&current=temperature_2m&timezone=auto"></a>
     */
    public OpenMeteoResponseDto fetchCurrentTemperature(CoordinatesWGS84 coordinates) {
        log.info("Fetching weather for lat: {}, lon: {}", coordinates.latitude(), coordinates.longitude());

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/forecast")
                        .queryParam("latitude", coordinates.latitude())
                        .queryParam("longitude", coordinates.longitude())
                        .queryParam("current", "temperature_2m")
                        .queryParam("timezone", "auto")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new RuntimeException("OpenMeteo API error: " + response.getStatusCode());
                })
                .body(OpenMeteoResponseDto.class);
    }
}
