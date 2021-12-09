package com.programmers.film.api.post.dto.common;

import lombok.Builder;

@Builder
public class SimplePostDto {
    private Long postId;
    private String state;
    private PointDto location;
}
