package com.programmers.film.api.post.converter;

import com.programmers.film.api.post.dto.common.AuthorityImageDto;
import com.programmers.film.api.post.dto.common.OrderImageUrlDto;
import com.programmers.film.api.post.dto.request.CreatePostRequest;
import com.programmers.film.api.post.dto.response.CreatePostResponse;
import com.programmers.film.api.post.dto.response.DeletePostResponse;
import com.programmers.film.api.post.dto.response.GetPostDetailResponse;
import com.programmers.film.api.post.dto.response.PreviewPostResponse;
import com.programmers.film.domain.common.domain.ImageUrl;
import com.programmers.film.domain.post.domain.Post;
import com.programmers.film.domain.post.domain.PostAuthority;
import com.programmers.film.domain.post.domain.PostDetail;
import com.programmers.film.domain.post.domain.PostState;
import com.programmers.film.domain.post.domain.PostStatus;
import com.programmers.film.domain.post.repository.PostRepository;
import com.programmers.film.domain.post.repository.PostStateRepository;
import com.programmers.film.domain.user.domain.User;
import com.programmers.film.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostConverter {
    private final PointConverter pointConverter;
    private final PostStateRepository postStateRepository;

    @Transactional(readOnly = true)
    public List<AuthorityImageDto> getAuthorityImageList(Post post) {
        AtomicInteger index = new AtomicInteger();

        return post.getPostAuthorities().stream()
            .map(
                postAuthority -> {
                    User user = postAuthority.getUser();
                    ImageUrl profileImageUrl = user.getProfileImageUrl();
                    if(profileImageUrl != null) {
                        return AuthorityImageDto.builder()
                            .authorityId(user.getId())
                            .imageOrder(index.getAndIncrement())
                            .imageUrl(profileImageUrl.getOriginalSizeUrl())
                            .build();
                    }
                    return AuthorityImageDto.builder()
                        .authorityId(user.getId())
                        .imageOrder(index.getAndIncrement())
                        .imageUrl(null)
                        .build();
                }
            ).toList();
    }

    public Post createPostRequestToPost(CreatePostRequest request, User authorUser) {
        PostState postState = postStateRepository.findByState(PostStatus.CLOSED.toString()).get();
        return Post.builder()
            .title(request.getTitle())
            .previewText(request.getPreviewText())
            .location(pointConverter.stringPointToDoublePoint(request.getLocation()))
            .availableAt(request.getAvailableAt())
            .author(authorUser)
            .state(postState)
            .postAuthorities(new ArrayList<>())
            .build();
    }

    public CreatePostResponse postToCreatePostResponse(Post post){
        return CreatePostResponse.builder()
            .postId(post.getId())
            .title(post.getTitle())
            .previewText(post.getPreviewText())
            .availableAt(post.getAvailableAt())
            .state(post.getState().getState())
            .location(pointConverter.doublePointToStringPoint(post.getLocation()))
            .authorityCount(1)
            .authorityImageList(getAuthorityImageList(post))
            .build();
    }

    @Transactional(readOnly = true)
    public PreviewPostResponse postToPreviewPostResponse(Post post) {
        List<PostAuthority> postAuthorities = post.getPostAuthorities();
        return PreviewPostResponse.builder()
            .postId(post.getId())
            .title(post.getTitle())
            .previewText(post.getPreviewText())
            .availableAt(post.getAvailableAt())
            .state(post.getState().getState())
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

    public GetPostDetailResponse postToGetPostDetailResponse(Post post, PostDetail postDetail) {
        boolean isOpened = postDetail.getOpener() != null;
        return GetPostDetailResponse.builder()
            .authorityImageList(getAuthorityImageList(post))
            .postId(post.getId())
            .title(post.getTitle())
            .content(postDetail.getContent())
            .imageUrls(getImageUrls(postDetail))
            .authorNickname(post.getAuthor().getNickname())
            .authorImageUrl(post.getAuthor().getProfileImageUrl().getOriginalSizeUrl())
            .createdAt(post.getCreatedAt().toLocalDate())
            .location(pointConverter.doublePointToStringPoint(post.getLocation()))
            .openedAt(postDetail.getOpenedAt())
            .openerNickname(postDetail.getOpener().getNickname())
            .openerImageUrl(postDetail.getOpener().getProfileImageUrl().getOriginalSizeUrl())
            .isOpened(isOpened)
            .openedAt(postDetail.getOpenedAt())
            .previewText(post.getPreviewText())
            .build();
    }

    @Transactional(readOnly = true)
    public List<OrderImageUrlDto> getImageUrls(PostDetail postDetail) {
        List<OrderImageUrlDto> imgUrls = new ArrayList<>();
        for (var postImage : postDetail.getPostImages()) {
            OrderImageUrlDto orderImage = OrderImageUrlDto.builder()
                .imageOrder(postImage.getOrder())
                .imageUrl(postImage.getImageUrl().getOriginalSizeUrl())
                .build();

            imgUrls.add(orderImage);
        }
        return imgUrls;
    }
}
