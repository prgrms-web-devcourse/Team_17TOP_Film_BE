package com.programmers.film.api.post.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.programmers.film.api.post.dto.common.AuthorityImageDto;
import com.programmers.film.api.post.dto.common.PointDto;
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
    private String authorNickname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate availableAt;
    private String state;
    private PointDto location;

    private int authorityCount;
    private List<AuthorityImageDto> authorityImageList;
}
