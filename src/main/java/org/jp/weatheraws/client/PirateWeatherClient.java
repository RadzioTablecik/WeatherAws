package org.jp.weatheraws.client;

import lombok.extern.slf4j.Slf4j;
import org.jp.weatheraws.dto.pirateweather.PirateWeatherResponseDto;
import org.jp.weatheraws.model.CoordinatesWGS84;
import org.jp.weatheraws.model.WeatherData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class PirateWeatherClient implements WeatherProvider {
    private final RestClient restClient;
    private final String apiKey;

    public PirateWeatherClient(RestClient.Builder builder,
                               @Value("${clients.weather.pirateweather.url}") String url,
                               @Value("${clients.weather.pirateweather.key}") String apiKey) {
        this.restClient = builder.baseUrl(url).build();
        this.apiKey = apiKey;

        log.info("API KEY: {}", this.apiKey);
    }

    /**
     * Fetches weather data from PirateWeather API.
     * Sample request:
     * <a href="https://api.pirateweather.net/forecast/{api_key}/{lat_and_long_or_time}?exclude=&extend=&lang=&units=&version=&tmextra=&icon="></a>
     */
    @Override
    public WeatherData fetchCurrentTemperature(CoordinatesWGS84 coordinates) {
        log.info("[Pirate Weather API] Fetching weather for lat: {}, lon: {}",
                coordinates.latitude(), coordinates.longitude());

        String location = coordinates.latitude() + "," + coordinates.longitude();

        PirateWeatherResponseDto response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/forecast/{key}/{location}")
                        .queryParam("units", "ca")
                        .build(apiKey, location))
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, res) -> {
                    throw new RuntimeException("PirateWeather error: " + res.getStatusCode());
                })
                .body(PirateWeatherResponseDto.class);

        String formattedTime = java.time.Instant.ofEpochSecond(response.currently().time()).toString();

        return new WeatherData(
                response.currently().temperature(),
                formattedTime
        );
    }

    @Override
    public boolean supports(String providerName) {
        return "pirateweather".equalsIgnoreCase(providerName);
    }
}
