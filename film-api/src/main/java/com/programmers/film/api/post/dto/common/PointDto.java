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
        PointDto pointDto = (PointDto) o;
        return Objects.equals(getLatitude(), pointDto.getLatitude())
            && Objects.equals(getLongitude(), pointDto.getLongitude());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLatitude(), getLongitude());
    }
}
