package com.programmers.film.api.post.converter;

import com.programmers.film.api.post.dto.common.PointDto;
import com.programmers.film.domain.common.domain.Point;
import org.springframework.stereotype.Component;

@Component
public class PointConverter {
    PointDto doublePointToStringPoint(Point point) {
        return PointDto.builder()
            .latitude(point.getLatitude().toString())
            .longitude(point.getLongitude().toString())
            .build();
    }

    Point stringPointToDoublePoint(PointDto pointDto) {
        return Point.builder()
            .longitude(Double.parseDouble(pointDto.getLongitude()))
            .latitude(Double.parseDouble(pointDto.getLatitude()))
            .build();
    }
}
