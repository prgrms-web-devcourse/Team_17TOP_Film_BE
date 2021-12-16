package com.programmers.film.api.post.dto.common;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class SimpleFixAuthorityDto {
    private Long userId;
    private Boolean addOrDelete;
}
