package com.programmers.film.domain.common.domain;

import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter @Builder
@NoArgsConstructor
@Embeddable
@AllArgsConstructor
public class ImageUrl {
    private String originalSizeUrl;
    private String smallSizeUrl;

    public void setOriginalSizeUrl(String originalSizeUrl) {
        this.originalSizeUrl = originalSizeUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImageUrl imageUrl = (ImageUrl) o;
        return Objects.equals(getOriginalSizeUrl(), imageUrl.getOriginalSizeUrl())
            && Objects.equals(getSmallSizeUrl(), imageUrl.getSmallSizeUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOriginalSizeUrl(), getSmallSizeUrl());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("originalSizeUrl", originalSizeUrl)
            .append("smallSizeUrl", smallSizeUrl)
            .toString();
    }
}
