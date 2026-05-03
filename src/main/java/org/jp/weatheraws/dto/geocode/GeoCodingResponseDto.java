package org.jp.weatheraws.dto.geocode;

import java.util.List;

public record GeoCodingResponseDto(
        List<CityDataDto> results
) {}
