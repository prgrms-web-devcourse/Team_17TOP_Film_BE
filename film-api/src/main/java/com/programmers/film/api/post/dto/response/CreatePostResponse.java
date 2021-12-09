package com.programmers.film.api.post.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.programmers.film.api.post.dto.common.AuthorityImage;
import com.programmers.film.api.post.dto.common.PointDto;
import com.programmers.film.domain.post.domain.PostState;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public class CreatePostResponse {

    private Long postId;

    private String title;

    private String previewText;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate availableAt;

    private PostState state;

    private PointDto location;

    private int authorityCount;

    private List<AuthorityImage> authorityImageList;
}
