package com.programmers.film.api.post.dto.request;

import com.programmers.film.api.post.dto.common.OrderImageFileDto;
import com.programmers.film.api.post.dto.common.PointDto;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
@Getter
public class CreatePostRequestString {
    private String title;

    private String previewText;

    private String latitude;

    private String longitude;

    private String availableAt;

    private String content;

}
