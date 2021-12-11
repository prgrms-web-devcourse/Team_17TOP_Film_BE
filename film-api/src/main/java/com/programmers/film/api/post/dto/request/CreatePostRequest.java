package com.programmers.film.api.post.dto.request;

import com.programmers.film.api.post.dto.common.OrderImageFileDto;
import com.programmers.film.api.post.dto.common.PointDto;
import java.time.LocalDate;
import java.util.ArrayList;
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

    private List<OrderImageFileDto> imageFiles;

    private String content;

    private Long authorUserId;

    public void setImageFiles(String[] urls) {
        List<OrderImageFileDto> temp = new ArrayList<>();
        int i = 0;
        for (String url : urls) {
            temp.add(new OrderImageFileDto(i++, url));
        }
        this.imageFiles = temp;
    }
}
