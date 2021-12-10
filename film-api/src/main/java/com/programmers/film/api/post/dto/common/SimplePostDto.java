package com.programmers.film.api.post.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder @Getter
@AllArgsConstructor
@NoArgsConstructor
public class SimplePostDto {
    private Long postId;
    private String state;
    private PointDto location;
}
