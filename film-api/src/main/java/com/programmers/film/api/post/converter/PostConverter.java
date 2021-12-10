package com.programmers.film.api.post.converter;

import com.programmers.film.api.post.dto.common.AuthorityImageDto;
import com.programmers.film.api.post.dto.common.OrderImageUrlDto;
import com.programmers.film.api.post.dto.request.CreatePostRequest;
import com.programmers.film.api.post.dto.response.CreatePostResponse;
import com.programmers.film.api.post.dto.response.DeletePostResponse;
import com.programmers.film.api.post.dto.response.GetPostDetailResponse;
import com.programmers.film.api.post.dto.response.PreviewPostResponse;
import com.programmers.film.api.post.exception.PostIdNotFoundException;
import com.programmers.film.api.user.exception.UserIdNotFoundExceoption;
import com.programmers.film.domain.common.domain.ImageUrl;
import com.programmers.film.domain.post.domain.Post;
import com.programmers.film.domain.post.domain.PostAuthority;
import com.programmers.film.domain.post.domain.PostDetail;
import com.programmers.film.domain.post.domain.PostState;
import com.programmers.film.domain.post.domain.PostStatus;
import com.programmers.film.domain.post.repository.PostDetailRepository;
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
    private final PostRepository postRepository;
    private final PostDetailRepository postDetailRepository;
    private final UserRepository userRepository;

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
        PostState postState = postStateRepository.findByPostStateValue(PostStatus.CLOSED.toString()).get();
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
            .state(post.getState().getPostStateValue())
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
            .state(post.getState().getPostStateValue())
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

    @Transactional(readOnly = true)
    public GetPostDetailResponse postToGetPostDetailResponse(Long postId, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserIdNotFoundExceoption("사용자를 찾을 수 없습니다."));
        PostDetail postDetail = postDetailRepository.findByPostId(postId)
            .orElseThrow(() -> new PostIdNotFoundException("게시물을 찾을 수 없습니다."));
        Post post = postDetail.getPost();

        User author = post.getAuthor();
        ImageUrl authorProfileImageUrl = author.getProfileImageUrl();
        ImageUrl openerProfileImageUrl = user.getProfileImageUrl();

        return GetPostDetailResponse.builder()
            .authorityImageList(getAuthorityImageList(post))
            .postId(post.getId())
            .title(post.getTitle())
            .content(postDetail.getContent())
            .imageUrls(getImageUrls(postDetail))
            .authorNickname(author.getNickname())
            .authorImageUrl(authorProfileImageUrl != null ? authorProfileImageUrl.getOriginalSizeUrl() : null)
            .createdAt(post.getCreatedAt().toLocalDate())
            .location(pointConverter.doublePointToStringPoint(post.getLocation()))
            .openedAt(postDetail.getOpenedAt())
            .openerNickname(user.getNickname())
            .openerImageUrl(openerProfileImageUrl != null ? openerProfileImageUrl.getOriginalSizeUrl() : null)
            .isOpened(true)
            .openedAt(postDetail.getOpenedAt())
            .previewText(post.getPreviewText())
            .build();
    }

    @Transactional(readOnly = true)
    public GetPostDetailResponse postToGetPostDetailResponse(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new UserIdNotFoundExceoption("사용자를 찾을 수 없습니다."));
        PostDetail postDetail = postDetailRepository.findByPostId(postId)
            .orElseThrow(() -> new PostIdNotFoundException("게시물을 찾을 수 없습니다."));

        User author = post.getAuthor();
        ImageUrl authorProfileImageUrl = author.getProfileImageUrl();
        User opener = postDetail.getOpener();
        ImageUrl openerProfileImageUrl = opener != null ? opener.getProfileImageUrl() : null;

        return GetPostDetailResponse.builder()
            .authorityImageList(getAuthorityImageList(post))
            .postId(post.getId())
            .title(post.getTitle())
            .content(postDetail.getContent())
            .imageUrls(getImageUrls(postDetail))
            .authorNickname(author.getNickname())
            .authorImageUrl(authorProfileImageUrl != null ? authorProfileImageUrl.getOriginalSizeUrl() : null)
            .createdAt(post.getCreatedAt().toLocalDate())
            .location(pointConverter.doublePointToStringPoint(post.getLocation()))
            .openedAt(postDetail.getOpenedAt())
            .openerNickname(opener.getNickname())
            .openerImageUrl(openerProfileImageUrl != null ? openerProfileImageUrl.getOriginalSizeUrl() : null)
            .isOpened(false)
            .openedAt(postDetail.getOpenedAt())
            .previewText(post.getPreviewText())
            .build();
    }

    @Transactional(readOnly = true)
    public List<OrderImageUrlDto> getImageUrls(PostDetail postDetail) {
        List<OrderImageUrlDto> imgUrls = new ArrayList<>();
        for (var postImage : postDetail.getPostImages()) {
            OrderImageUrlDto orderImage = OrderImageUrlDto.builder()
                .imageOrder(postImage.getImageOrder())
                .imageUrl(postImage.getImageUrl().getOriginalSizeUrl())
                .build();

            imgUrls.add(orderImage);
        }
        return imgUrls;
    }
}
