package org.jp.weatheraws.model;

import org.springframework.util.Assert;

/**
 * City name in proper format. Record is self validating Value Object.
 */
public record City(String name) {
    private static final String VALID_CITY_REGEX = "^[a-zA-Z\\p{L}\\s\\-]*$";

    public City {
        Assert.hasText(name, "City name cannot be empty");
        Assert.isTrue(name.matches(VALID_CITY_REGEX),
                () -> "Invalid city name format: " + name);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
