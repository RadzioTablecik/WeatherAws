package org.jp.weatheraws.dto.openmeteo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrentDto(
        @JsonProperty("temperature_2m")
        Double temperature,
        String time
) {}

