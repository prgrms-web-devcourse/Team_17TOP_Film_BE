package com.programmers.film.api.post.converter;

import com.programmers.film.api.post.dto.request.CreatePostRequest;
import com.programmers.film.api.post.dto.response.CreatePostResponse;
import com.programmers.film.domain.post.domain.Post;
import org.springframework.stereotype.Component;

@Component
public class PostConverter {

    public Post createPostRequestToPost(CreatePostRequest request) {
        Post post = Post.builder()
            .title(request.getTitle())
            .previewText(request.getPreviewText())
                .location(request.getLocation())
                    .availableAt(request.getAvailableAt())
                        .author()

            build()
    }
}
