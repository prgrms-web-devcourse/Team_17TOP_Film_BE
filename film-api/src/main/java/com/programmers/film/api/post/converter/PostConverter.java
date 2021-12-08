package com.programmers.film.api.post.converter;

import com.programmers.film.api.post.dto.common.AuthorityImage;
import com.programmers.film.api.post.dto.request.CreatePostRequest;
import com.programmers.film.api.post.dto.response.CreatePostResponse;
import com.programmers.film.api.post.dto.response.DeletePostResponse;
import com.programmers.film.api.post.dto.response.PreviewPostResponse;
import com.programmers.film.domain.member.domain.User;
import com.programmers.film.domain.member.repository.UserRepository;
import com.programmers.film.domain.post.domain.Post;
import com.programmers.film.domain.post.domain.PostAuthority;
import com.programmers.film.domain.post.repository.PostRepository;
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
        List<PostAuthority> postAuthorities = post.getPostAuthorities();
        List<AuthorityImage> authorityImageList = new ArrayList<>();
        for(int i = 0; i < postAuthorities.size(); i++) {
            PostAuthority postAuthority = postAuthorities.get(i);
            authorityImageList.add(
                AuthorityImage.builder()
                    .authorityId(postAuthority.getUser().getId())
                    .imageOrder(i)
                    .imageUrl(postAuthority.getUser().getProfileImageUrl().getOriginalSizeUrl())
                    .build()
            );
        }
        return authorityImageList;
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
  
    @Transactional(readOnly = true)
    public PreviewPostResponse PostToPreviewPostResponse(Post post) {
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
  
   public DeletePostResponse PostToDeletePostResponse(Post post) {
        return DeletePostResponse.builder()
            .postId(post.getId())
            .build();
    }
}
