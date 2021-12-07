package com.programmers.film.api.post.dto.request;

import com.programmers.film.api.post.dto.common.OrderImage;
import com.programmers.film.domain.common.domain.Point;
import com.programmers.film.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Builder
@Getter
public class CreatePostRequest {

    private String title;
    private String previewText;
    private Point location;
    private String availableAt;
    private List<String> authorities;
    private List<OrderImage> imageFiles;
    private String content;
    private String authorNickname;

}
