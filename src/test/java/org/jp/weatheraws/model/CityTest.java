package org.jp.weatheraws.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
public class CityTest {

    @Test
    public void shouldCreateCityForValidName() {
        // GIVEN
        String validName = "Wrocław";

        // WHEN
        City city = new City(validName);

        // THEN
        assertThat(city.name()).isEqualTo(validName);
        assertThat(city.toString()).isEqualTo(validName);
    }

    @ParameterizedTest
    @ValueSource(strings = {"New York", "Bielsko-Biala", "Lodz", "Saint-Tropez"})
    public void shouldCreateCityForVariousValidFormats(String cityName) {
        // WHEN
        City city = new City(cityName);

        // THEN
        assertThat(city.name()).isEqualTo(cityName);
    }

    @Test
    public void shouldThrowExceptionWhenCityNameIsEmpty() {
        // THEN
        assertThatThrownBy(() -> new City(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("City name cannot be empty");
    }

    @Test
    public void shouldThrowExceptionWhenCityNameIsBlank() {
        // THEN
        assertThatThrownBy(() -> new City("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("City name cannot be empty");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Wroclaw123", "City!", "London@", "Praha_"})
    public void shouldThrowExceptionForInvalidCharacters(String invalidName) {
        // THEN
        assertThatThrownBy(() -> new City(invalidName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid city name format");
    }
}
