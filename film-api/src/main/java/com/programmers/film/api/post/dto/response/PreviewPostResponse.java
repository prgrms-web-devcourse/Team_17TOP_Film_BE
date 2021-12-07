package com.programmers.film.api.post.dto.response;

import com.programmers.film.api.post.dto.common.AuthorityImage;
import com.programmers.film.domain.common.domain.Point;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PreviewPostResponse {
    private Long postId;
    private String title;
    private String previewText;

    private LocalDate availableAt;
    private Point location;

    private int authorityCount;
    private List<AuthorityImage> authorityImageList;
}
