package com.programmers.film.api.post.dto.common;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class OrderImageUrlDto {

    private int imageOrder;
    private String imageUrl;
}
