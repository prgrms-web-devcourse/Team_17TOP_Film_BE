package com.programmers.film.api.post.dto.common;

import com.programmers.film.domain.common.domain.Point;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class PointDto {
    private String latitude;
    private String longitude;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        return Objects.equals(getLatitude(), point.getLatitude()) && Objects.equals(
            getLongitude(), point.getLongitude());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLatitude(), getLongitude());
    }
}
