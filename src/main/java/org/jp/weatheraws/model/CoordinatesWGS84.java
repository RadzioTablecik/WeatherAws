package org.jp.weatheraws.model;
import org.springframework.util.Assert;

/**
 * Coordinates in WGS84 format. Record is self validating Value Object.
 */
public record CoordinatesWGS84(
        double latitude,
        double longitude
) {
        public CoordinatesWGS84 {
                Assert.isTrue(latitude >= -90 && latitude <= 90,
                        "Latitude must be between -90 and 90. Provided: " + latitude);
                Assert.isTrue(longitude >= -180 && longitude <= 180,
                        "Longitude must be between -180 and 180. Provided: " + longitude);
        }
}
