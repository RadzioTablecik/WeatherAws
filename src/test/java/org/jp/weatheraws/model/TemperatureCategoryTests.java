package org.jp.weatheraws.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TemperatureCategoryTests {

    @ParameterizedTest
    @CsvSource({
            "35.0, HOT",
            "30.0, HOT",
            "25.0, WARM",
            "20.0, WARM",
            "15.0, MILD",
            "10.0, MILD",
            "5.0,  COLD",
            "0.0,  COLD",
            "-5.0, FREEZING"
    })
    void shouldReturnCorrectCategoryForTemperature(double temp, TemperatureCategory expected) {
        assertEquals(expected, TemperatureCategory.from(temp));
    }

    @Test
    void shouldReturnFreezingForExtremelyLowTemperature() {
        assertEquals(TemperatureCategory.FREEZING, TemperatureCategory.from(-273.15));
    }
}
