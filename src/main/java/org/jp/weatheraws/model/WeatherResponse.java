package org.jp.weatheraws.model;

public record WeatherResponse (
    String city,
    Double temperature,
    String time,
    TemperatureCategory temperatureCategory,
    String provider
) {}
