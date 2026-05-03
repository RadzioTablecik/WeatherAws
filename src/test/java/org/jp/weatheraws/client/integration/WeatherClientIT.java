package org.jp.weatheraws.client.integration;

import org.jp.weatheraws.client.WeatherClient;
import org.jp.weatheraws.config.ClientConfig;
import org.jp.weatheraws.dto.openmeteo.OpenMeteoResponseDto;
import org.jp.weatheraws.model.CoordinatesWGS84;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("integration")
@SpringBootTest(classes = {WeatherClient.class, ClientConfig.class})
@Disabled("Manual verification: Requires real API connectivity")
public class WeatherClientIT {

    @Autowired
    private WeatherClient weatherClient;

    @Test
    void shouldFetchRealDataFromOpenMeteo() {
        // GIVEN
        CoordinatesWGS84 coordinatesWGS84 = new CoordinatesWGS84(
                51.1,
                17.0333
        );

        // WHEN
        OpenMeteoResponseDto response = weatherClient.fetchCurrentTemperature(coordinatesWGS84);

        // THEN
        assertNotNull(response, "Response should not be null");
        assertNotNull(response.current(), "Current weather data should be present");

        assertTrue(response.current().temperature() > -100 && response.current().temperature() < 100,
                "Temperature should be within a realistic range");

        System.out.println("Temperature in Wroclaw: " + response.current().temperature() + "°C at " + response.current().time());
    }
}
