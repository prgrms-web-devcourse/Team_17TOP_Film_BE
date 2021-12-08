package com.programmers.film.api.post.dto.response;

import com.programmers.film.api.post.dto.common.SimplePostDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class GetMapResponse {
    private List<SimplePostDto> posts;

    public GetMapResponse() {
        this.posts = new ArrayList<>();
    }

    public GetMapResponse(List<SimplePostDto> posts) {
        this.posts = posts;
    }
}
