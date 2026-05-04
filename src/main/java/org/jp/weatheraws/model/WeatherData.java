package org.jp.weatheraws.model;

public record WeatherData(
        double temperature,
        String time
) {}
