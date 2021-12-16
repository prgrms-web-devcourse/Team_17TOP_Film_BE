package com.programmers.film.api.post.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.programmers.film.api.post.dto.common.AuthorityImageDto;
import com.programmers.film.api.post.dto.common.OrderImageUrlDto;
import com.programmers.film.api.post.dto.common.PointDto;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder @Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetPostDetailResponse {

    private Long postId;

    private String title;

    private String content;

    private List<OrderImageUrlDto> imageUrls;

    private String authorNickname; // 작성자 이름

    private String authorImageUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate createdAt;

    private List<AuthorityImageDto> authorityImageList;

    private PointDto location;

    private String openerNickname;

    private String openerImageUrl;

    private Boolean isOpened;

    private LocalDate openedAt;

    private String previewText;

}
