package com.programmers.film.api.post.dto.response;

import com.programmers.film.api.post.dto.common.SimplePostDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class MapResponse {
    private List<SimplePostDto> posts;
}
