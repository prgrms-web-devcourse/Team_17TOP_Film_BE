package com.programmers.film.api.post.dto.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthorityImage {
    private int imageOrder;
    private Long authorityId;
    private String imageUrl;
}
