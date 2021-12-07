package com.programmers.film.api.post.converter;

import com.programmers.film.api.post.dto.common.AuthorityImage;
import com.programmers.film.api.post.dto.request.CreatePostRequest;
import com.programmers.film.api.post.dto.response.CreatePostResponse;
import com.programmers.film.domain.member.domain.User;
import com.programmers.film.domain.member.repository.UserRepository;
import com.programmers.film.domain.post.domain.Post;
import com.programmers.film.domain.post.domain.PostAuthority;
import com.programmers.film.domain.post.repository.PostRepository;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PostConverter {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public PostConverter(
        UserRepository userRepository,
        PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }


    public Post createPostRequestToPost(CreatePostRequest request) {
        User authorUser = userRepository.findById(request.getAuthorUserId()).get(); // Exception
        Post post = Post.builder()
            .title(request.getTitle())
            .previewText(request.getPreviewText())
            .location(request.getLocation())
            .availableAt(request.getAvailableAt())
            .author(authorUser)
            .build();
        return post;
    }

    public CreatePostResponse postToCreatePostResponse(Post post){
        CreatePostResponse createPostResponse = CreatePostResponse.builder()
            .postId(post.getId())
            .title(post.getTitle())
            .previewText(post.getPreviewText())
            .availableAt(post.getAvailableAt())
            // TODO : state 설정
            .location(post.getLocation())
            .authorityCount(1)
            .authorityImageList(getAuthorityImageList(post))
            .build();
        return createPostResponse;
    }

    public List<AuthorityImage> getAuthorityImageList(Post post) {
        return new ArrayList<>();
    }
}
