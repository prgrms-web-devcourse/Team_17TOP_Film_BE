package com.programmers.film.api.post.dto.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SimpleAuthorityDto {
    private int imageOrder;
    private Long authorityId;
    private String authorityNickName;
    private String imageUrl;
}
