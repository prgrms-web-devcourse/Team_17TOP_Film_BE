package com.programmers.film.api.post.dto.request;

import com.programmers.film.api.post.dto.common.OrderImageFileDto;
import com.programmers.film.api.post.dto.common.PointDto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public void setImageFiles(String[] urls) {
        List<OrderImageFileDto> temp = new ArrayList<>();
        int i = 0;
        for (String url : urls) {
            temp.add(new OrderImageFileDto(i++, url));
        }
        this.imageFiles = temp;
    }

    public void mappingString(CreatePostRequestString str) {
        this.title = str.getTitle();
        this.previewText = str.getPreviewText();
        this.location = PointDto.builder()
            .longitude(str.getLongitude())
            .latitude(str.getLatitude())
            .build();
        this.availableAt = LocalDate.parse(str.getAvailableAt(), DateTimeFormatter.ISO_DATE);
        this.authorities = new ArrayList<>();
        this.content = str.getContent();
    }
}
