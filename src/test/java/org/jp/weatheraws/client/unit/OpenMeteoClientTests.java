package org.jp.weatheraws.client.unit;

import org.jp.weatheraws.client.OpenMeteoClient;
import org.jp.weatheraws.model.CoordinatesWGS84;
import org.jp.weatheraws.model.WeatherData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestClient;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@Tag("unit")
public class OpenMeteoClientTests {

    private OpenMeteoClient openMeteoClient;
    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        RestClient.Builder builder = RestClient.builder();

        mockServer = MockRestServiceServer.bindTo(builder).build();
        openMeteoClient = new OpenMeteoClient(builder, "https://api.open-meteo.com/v1");
    }

    @Test
    public void shouldParseWeatherJsonCorrectly() {
        // GIVEN
        String jsonResponse = """
            {
                "current": {
                    "temperature_2m": 22.5,
                    "time": "2026-05-03T13:00"
                }
            }
            """;

        CoordinatesWGS84 coordinatesWGS84 = new CoordinatesWGS84(
                51.1,
                17.0333
        );

        mockServer.expect(once(), requestTo(startsWith("https://api.open-meteo.com/v1/forecast")))
                .andExpect(queryParam("latitude", String.valueOf(coordinatesWGS84.latitude())))
                .andExpect(queryParam("longitude", String.valueOf(coordinatesWGS84.longitude())))
                .andExpect(queryParam("current", "temperature_2m"))
                .andExpect(queryParam("timezone", "auto"))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        // WHEN
        WeatherData result = openMeteoClient.fetchCurrentTemperature(coordinatesWGS84);

        // THEN
        assertEquals(22.5, result.temperature());
        assertEquals("2026-05-03T13:00", result.time());
        mockServer.verify();
    }

    @Test
    public void shouldThrowExceptionWhenApiReturnsError() {
        // GIVEN
        CoordinatesWGS84 coords = new CoordinatesWGS84(
                51.1,
                17.0333
        );

        mockServer.expect(once(), requestTo(startsWith("https://api.open-meteo.com/v1/forecast")))
                .andRespond(MockRestResponseCreators.withBadRequest());

        // WHEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            openMeteoClient.fetchCurrentTemperature(coords);
        });

        // THEN
        assertEquals("OpenMeteo API error: 400 BAD_REQUEST", exception.getMessage());
    }
}
