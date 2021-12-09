package com.programmers.film.api.post.dto.request;

import com.programmers.film.api.post.dto.common.OrderImageFile;
import com.programmers.film.api.post.dto.common.PointDto;
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

    private PointDto location;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate availableAt;

    private List<Long> authorities;

    private List<OrderImageFile> imageFiles;

    private String content;

    private Long authorUserId;
}
