package org.jp.weatheraws.client.integration;

import org.jp.weatheraws.client.OpenMeteoClient;
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
@SpringBootTest(classes = {OpenMeteoClient.class, ClientConfig.class})
@Disabled("Manual verification: Requires real API connectivity")
public class OpenMeteoClientIT {

    @Autowired
    private OpenMeteoClient openMeteoClient;

    @Test
    public void shouldFetchRealDataFromOpenMeteo() {
        // GIVEN
        CoordinatesWGS84 coordinatesWGS84 = new CoordinatesWGS84(
                51.1,
                17.0333
        );

        // WHEN
        WeatherData response = openMeteoClient.fetchCurrentTemperature(coordinatesWGS84);

        // THEN
        assertNotNull(response, "Response should not be null");

        assertTrue(response.temperature() > -100 && response.temperature() < 100,
                "Temperature should be within a realistic range");

        System.out.println("Temperature in Wroclaw: " + response.temperature() + "°C at " + response.time());
    }
}
