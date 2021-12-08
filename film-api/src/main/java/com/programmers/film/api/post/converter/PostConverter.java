package com.programmers.film.api.post.converter;

import com.programmers.film.api.post.dto.common.AuthorityImage;
import com.programmers.film.api.post.dto.request.CreatePostRequest;
import com.programmers.film.api.post.dto.response.CreatePostResponse;
import com.programmers.film.api.post.dto.response.DeletePostResponse;
import com.programmers.film.api.post.dto.response.PreviewPostResponse;
import com.programmers.film.domain.post.domain.Post;
import com.programmers.film.domain.post.domain.PostAuthority;
import com.programmers.film.domain.post.repository.PostRepository;
import com.programmers.film.domain.user.domain.User;
import com.programmers.film.domain.user.repository.UserRepository;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostConverter {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PointConverter pointConverter;
  
    @Transactional(readOnly = true)
    public List<AuthorityImage> getAuthorityImageList(Post post) {
        AtomicInteger index = new AtomicInteger();

        return post.getPostAuthorities().stream()
            .map(
                postAuthority -> AuthorityImage.builder()
                    .authorityId(postAuthority.getUser().getId())
                    .imageOrder(index.getAndIncrement())
                    .imageUrl(postAuthority.getUser().getProfileImageUrl().getOriginalSizeUrl())
                    .build()
            ).toList();
    }

    public Post createPostRequestToPost(CreatePostRequest request) {
        User authorUser = userRepository.findById(request.getAuthorUserId()).get(); // Exception
        return Post.builder()
            .title(request.getTitle())
            .previewText(request.getPreviewText())
            .location(request.getLocation())
            .availableAt(request.getAvailableAt())
            .author(authorUser)
            .build();
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
  
    @Transactional(readOnly = true)
    public PreviewPostResponse postToPreviewPostResponse(Post post) {
        List<PostAuthority> postAuthorities = post.getPostAuthorities();
        return PreviewPostResponse.builder()
            .postId(post.getId())
            .title(post.getTitle())
            .previewText(post.getPreviewText())
            .availableAt(post.getAvailableAt())
            .location(pointConverter.doublePointToStringPoint(post.getLocation()))
            .authorityCount(postAuthorities.size())
            .authorityImageList(getAuthorityImageList(post))
            .build();
    }
  
   public DeletePostResponse postToDeletePostResponse(Long postId) {
        return DeletePostResponse.builder()
            .postId(postId)
            .build();
    }
}
