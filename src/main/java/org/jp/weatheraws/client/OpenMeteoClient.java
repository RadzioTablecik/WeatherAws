package org.jp.weatheraws.client;

import lombok.extern.slf4j.Slf4j;
import org.jp.weatheraws.dto.openmeteo.CurrentDto;
import org.jp.weatheraws.dto.openmeteo.OpenMeteoResponseDto;
import org.jp.weatheraws.model.CoordinatesWGS84;
import org.jp.weatheraws.model.WeatherData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class OpenMeteoClient implements WeatherProvider {
    private final RestClient restClient;

    public OpenMeteoClient(RestClient.Builder builder,
                           @Value("${clients.weather.openmeteo}") String url) {
        this.restClient = builder.baseUrl(url).build();
    }

    /**
     * Fetches current weather data from Open-Meteo API.
     * Sample request:
     * <a href="https://api.open-meteo.com/v1/forecast?latitude=51.1&longitude=17.0333&current=temperature_2m&timezone=auto"></a>
     */
    @Override
    public WeatherData fetchCurrentTemperature(CoordinatesWGS84 coordinates) {
        log.info("[Open-Meteo API] Fetching weather for lat: {}, lon: {}", coordinates.latitude(), coordinates.longitude());

        OpenMeteoResponseDto openMeteoResponseDto = restClient.get()
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

        CurrentDto currentDto = openMeteoResponseDto.current();

        return new WeatherData(currentDto.temperature(), currentDto.time());
    }

    @Override
    public boolean supports(String providerName) {
        return "openmeteo".equalsIgnoreCase(providerName);
    }
}
