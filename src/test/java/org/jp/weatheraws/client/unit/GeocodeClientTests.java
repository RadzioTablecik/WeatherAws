package org.jp.weatheraws.client.unit;

import org.jp.weatheraws.client.GeocodeClient;
import org.jp.weatheraws.dto.geocode.GeoCodingResponseDto;
import org.jp.weatheraws.model.City;
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
public class GeocodeClientTests {

    private GeocodeClient geocodeClient;
    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        RestClient.Builder builder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        geocodeClient = new GeocodeClient(builder, "https://geocoding-api.open-meteo.com/v1");
    }

    @Test
    public void shouldParseGeocodeJsonCorrectly() {
        // GIVEN
        String jsonResponse = """
            {
                "results": [
                    {
                        "latitude": 51.1,
                        "longitude": 17.0333
                    }
                ]
            }
            """;

        City city = new City("Wroclaw");

        mockServer.expect(once(), requestTo(startsWith("https://geocoding-api.open-meteo.com/v1/search")))
                .andExpect(queryParam("name", city.toString()))
                .andExpect(queryParam("count", "1"))
                .andExpect(queryParam("language", "en"))
                .andExpect(queryParam("format", "json"))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        // WHEN
        GeoCodingResponseDto result = geocodeClient.fetchCoordinates(city);

        // THEN
        assertEquals(51.1, result.results().getFirst().latitude());
        assertEquals(17.0333, result.results().getFirst().longitude());
        mockServer.verify();
    }

    @Test
    public void shouldThrowExceptionWhenCityNotFound() {
        // GIVEN
        String emptyResponse = "{\"results\": []}";
        City city = new City("NonExistent");

        mockServer.expect(once(), requestTo(startsWith("https://geocoding-api.open-meteo.com/v1/search")))
                .andRespond(withSuccess(emptyResponse, MediaType.APPLICATION_JSON));

        // THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            geocodeClient.fetchCoordinates(city);
        });

        assertEquals("City not found: " + city, exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenGeocodeApiReturnsError() {
        // GIVEN
        City city = new City("Wroclaw");

        mockServer.expect(once(), requestTo(startsWith("https://geocoding-api.open-meteo.com/v1/search")))
                .andRespond(MockRestResponseCreators.withServerError());

        // THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            geocodeClient.fetchCoordinates(city);
        });

        assertEquals("OpenMeteoGeocode API error: 500 INTERNAL_SERVER_ERROR", exception.getMessage());
    }
}
