package com.programmers.film.api.post.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreatePostRequestString {
    private String title;

    private String previewText;

    private String latitude;

    private String longitude;

    private String availableAt;

    private String content;

}
