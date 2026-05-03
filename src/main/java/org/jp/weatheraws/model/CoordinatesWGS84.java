package org.jp.weatheraws.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record CoordinatesWGS84(
        @Min(value = -90, message = "Latitude must be at least -90")
        @Max(value = 90, message = "Latitude must be at most 90")
        double latitude,

        @Min(value = -180, message = "Longitude must be at least -180")
        @Max(value = 180, message = "Longitude must be at most 180")
        double longitude
) {
}
