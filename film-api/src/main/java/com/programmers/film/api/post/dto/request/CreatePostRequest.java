package com.programmers.film.api.post.dto.request;

import com.programmers.film.api.post.dto.common.OrderImage;
import com.programmers.film.domain.common.domain.Point;
import com.programmers.film.domain.post.domain.Post;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
@Getter
public class CreatePostRequest {

    private String title;

    private String previewText;

    private Point location;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate availableAt;

    private List<Long> authorities;

    private List<OrderImage> imageFiles;

    private String content;

    private String authorNickname;
}
