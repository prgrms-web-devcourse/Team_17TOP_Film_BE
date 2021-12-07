package com.programmers.film.api.post.dto.common;

import com.programmers.film.domain.common.domain.Point;
import java.util.List;

public class PostDto {
    private Long postId;
    private String state;
    private List<Point> location;
}
