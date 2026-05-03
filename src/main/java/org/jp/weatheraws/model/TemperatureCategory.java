package org.jp.weatheraws.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum TemperatureCategory {
    HOT(30.0),
    WARM(20.0),
    MILD(10.0),
    COLD(0.0),
    FREEZING(Double.NEGATIVE_INFINITY);

    private final double minTemp;

    public static TemperatureCategory from(double temp) {
        return Arrays.stream(values())
                .filter(cat -> temp >= cat.minTemp)
                .findFirst()
                .orElse(FREEZING);
    }
}
