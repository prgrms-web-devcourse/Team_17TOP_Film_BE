package com.programmers.film.domain.common.domain;

import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Point {
    private Double latitude;
    private Double longitude;

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
