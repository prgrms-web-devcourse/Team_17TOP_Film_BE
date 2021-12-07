package com.programmers.film.api.post.dto.response;

import com.programmers.film.api.post.dto.common.AuthorityImage;
import com.programmers.film.api.post.dto.common.OrderImage;
import com.programmers.film.domain.common.domain.Point;
import com.programmers.film.domain.post.domain.PostState;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public class CreatePostResponse {

    private Long postId;
    private String title;
    private String previewText;
    private LocalDate availableAt;
    private PostState state;
    private Point location;

    private int authorityCount;
    private List<AuthorityImage> authorityImageList;
}
