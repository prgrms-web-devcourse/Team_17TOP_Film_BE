package com.programmers.film.api.post.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.programmers.film.api.post.dto.common.AuthorityImage;
import com.programmers.film.api.post.dto.common.PointDto;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate availableAt;
    private PointDto location;

    private int authorityCount;
    private List<AuthorityImage> authorityImageList;
}
