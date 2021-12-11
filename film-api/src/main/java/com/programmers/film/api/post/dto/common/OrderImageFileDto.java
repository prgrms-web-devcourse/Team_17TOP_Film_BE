package com.programmers.film.api.post.dto.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class OrderImageFileDto { // must have : only one img
    private int imageOrder;
    private MultipartFile image;
}
