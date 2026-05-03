package org.jp.weatheraws.model;

public record WeatherResponse (
    Double temperature,
    String time,
    TemperatureCategory temperatureCategory
) {}
