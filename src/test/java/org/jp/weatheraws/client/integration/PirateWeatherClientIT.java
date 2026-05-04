package org.jp.weatheraws.client.integration;

import org.jp.weatheraws.client.PirateWeatherClient;
import org.jp.weatheraws.config.ClientConfig;
import org.jp.weatheraws.model.CoordinatesWGS84;
import org.jp.weatheraws.model.WeatherData;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("integration")
@SpringBootTest(classes = {PirateWeatherClient.class, ClientConfig.class})
@Disabled("Manual verification: Requires real API key and connectivity")
public class PirateWeatherClientIT {

    @Autowired
    private PirateWeatherClient pirateWeatherClient;

    @Test
    public void shouldFetchRealDataFromPirateWeather() {
        // GIVEN
        CoordinatesWGS84 coordinates = new CoordinatesWGS84(51.1079, 17.0385);

        // WHEN
        WeatherData response = pirateWeatherClient.fetchCurrentTemperature(coordinates);

        // THEN
        assertNotNull(response, "Response should not be null");

        assertTrue(response.temperature() > -100 && response.temperature() < 100,
                "Temperature should be within a realistic range. Found: " + response.temperature());

        assertNotNull(response.time(), "Timestamp should not be null");

        System.out.println("Pirate Weather Data: "
                + response.temperature() + "°C at " + response.time());
    }
}
