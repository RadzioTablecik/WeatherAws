package org.jp.weatheraws.client;

import org.jp.weatheraws.model.CoordinatesWGS84;
import org.jp.weatheraws.model.WeatherData;

/**
 * Strategy pattern to separate weather providers implementations from service logic
 */
public interface WeatherProvider {
    WeatherData fetchCurrentTemperature(CoordinatesWGS84 coordinates);
    boolean supports(String providerName);
}
