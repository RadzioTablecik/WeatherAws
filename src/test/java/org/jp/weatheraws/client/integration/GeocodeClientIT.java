package org.jp.weatheraws.client.integration;

import org.jp.weatheraws.client.GeocodeClient;
import org.jp.weatheraws.config.ClientConfig;
import org.jp.weatheraws.model.CoordinatesWGS84;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
@SpringBootTest(classes = {GeocodeClient.class, ClientConfig.class})
@Disabled("Manual verification: Requires real API connectivity")
public class GeocodeClientIT {

    @Autowired
    private GeocodeClient geocodeClient;

    @Test
    public void shouldFetchRealDataFromGeocodingApi() {
        // GIVEN
        String cityName = "Wroclaw";

        // WHEN
        CoordinatesWGS84 response = geocodeClient.fetchCoordinates(cityName);

        // THEN
        assertAll(
                () -> assertNotNull(response),
                () -> assertTrue(Math.abs(51.1 - response.latitude()) < 0.5, "Latitude should be close to 51.1"),
                () -> assertTrue(Math.abs(17.0 - response.longitude()) < 0.5, "Longitude should be close to 17.0")
        );
    }
}
