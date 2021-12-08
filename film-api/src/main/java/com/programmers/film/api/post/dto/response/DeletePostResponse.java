package com.programmers.film.api.post.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DeletePostResponse {
    private Long postId;
}
