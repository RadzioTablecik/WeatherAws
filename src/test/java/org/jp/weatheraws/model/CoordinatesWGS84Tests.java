package org.jp.weatheraws.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
public class CoordinatesWGS84Tests {

    @Test
    public void shouldCreateCoordinatesForValidInput() {
        assertDoesNotThrow(() -> new CoordinatesWGS84(51.1, 17.03));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-90.1, 90.1})
    public void shouldThrowExceptionForInvalidLatitude(double invalidLat) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new CoordinatesWGS84(invalidLat, 0)
        );
        assertTrue(ex.getMessage().contains("Latitude"));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-180.1, 180.1})
    public void shouldThrowExceptionForInvalidLongitude(double invalidLon) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new CoordinatesWGS84(0, invalidLon)
        );
        assertTrue(ex.getMessage().contains("Longitude"));
    }
}
