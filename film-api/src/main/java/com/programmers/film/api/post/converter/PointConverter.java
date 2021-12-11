package com.programmers.film.api.post.converter;

import com.programmers.film.api.post.dto.common.PointDto;
import com.programmers.film.api.post.exception.MapLocationException;
import com.programmers.film.domain.common.domain.Point;
import org.springframework.stereotype.Component;

@Component
public class PointConverter {
    public PointDto doublePointToStringPoint(Point point) {
        try {
            return PointDto.builder()
                .latitude(point.getLatitude().toString())
                .longitude(point.getLongitude().toString())
                .build();
        } catch (Exception e) {
            throw new MapLocationException("지도 타입이 잘못 입력되어있습니다. 위치를 저장할 수 없습니다.");
        }
    }

    public Point stringPointToDoublePoint(PointDto pointDto) {
        try {
            return Point.builder()
            .longitude(Double.parseDouble(pointDto.getLongitude()))
            .latitude(Double.parseDouble(pointDto.getLatitude()))
            .build();
        } catch (NumberFormatException e) {
            throw new MapLocationException("지도 타입이 잘못 저장되어있습니다. 위치를 불러올 수 없습니다.");
        }
    }
}
