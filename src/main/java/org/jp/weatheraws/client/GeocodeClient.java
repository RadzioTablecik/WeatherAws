package org.jp.weatheraws.client;

import lombok.extern.slf4j.Slf4j;
import org.jp.weatheraws.dto.geocode.GeoCodingResponseDto;
import org.jp.weatheraws.model.City;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class GeocodeClient {
    private final RestClient restClient;

    public GeocodeClient(RestClient.Builder builder,
                         @Value("${clients.geocode.openmeteo}") String url) {
        this.restClient = builder.baseUrl(url).build();
    }

    /**
     * Fetches geocode data from Open-Meteo GeocodingAPI.
     * Sample request:
     * <a href="https://geocoding-api.open-meteo.com/v1/search?name=wroclaw&count=1&language=en&format=json"></a>
     */
    public GeoCodingResponseDto fetchCoordinates(City cityName) {
        log.info("Fetching coordinates for city: {}", cityName);

        GeoCodingResponseDto responseDto = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("name", cityName)
                        .queryParam("count", 1)
                        .queryParam("language", "en")
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new RuntimeException("OpenMeteoGeocode API error: " + response.getStatusCode());
                })
                .body(GeoCodingResponseDto.class);

        if (responseDto == null || responseDto.results() == null || responseDto.results().isEmpty()) {
            throw new RuntimeException("City not found: " + cityName);
        }

        return responseDto;
    }
}
