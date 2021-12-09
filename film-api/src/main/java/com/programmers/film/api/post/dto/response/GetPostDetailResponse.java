package com.programmers.film.api.post.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.programmers.film.api.post.dto.common.AuthorityImage;
import com.programmers.film.api.post.dto.common.OrderImageUrl;
import com.programmers.film.api.post.dto.common.PointDto;
import com.programmers.film.domain.common.domain.ImageUrl;
import com.programmers.film.domain.post.domain.PostState;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public class GetPostDetailResponse {

    private Long postId;

    private String title;

    private String content;

    private List<OrderImageUrl> imageUrls;

    private String authorNickname; // 작성자 이름

    private String authorImageUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate createdAt;

    private List<AuthorityImage> authorityImageList;

    private PointDto location;

    private String openerNickname;

    private String openerImageUrl;

    private boolean isOpened;

    private LocalDate openedAt;

    private String previewText;

}
