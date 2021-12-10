package com.programmers.film.api.post.service;

import com.programmers.film.api.post.converter.PostConverter;
import com.programmers.film.api.post.dto.request.CreatePostRequest;
import com.programmers.film.api.post.dto.response.CreatePostResponse;
import com.programmers.film.api.post.dto.response.DeletePostResponse;
import com.programmers.film.api.post.dto.response.GetPostDetailResponse;
import com.programmers.film.api.post.dto.response.PreviewPostResponse;
import com.programmers.film.api.post.exception.PostIdNotFoundException;
import com.programmers.film.api.post.exception.PostCanNotOpenException;
import com.programmers.film.api.user.exception.UserIdNotFoundExceoption;
import com.programmers.film.common.error.exception.InvalidInputValueException;
import com.programmers.film.domain.common.domain.ImageUrl;
import com.programmers.film.domain.post.domain.Post;
import com.programmers.film.domain.post.domain.PostAuthority;
import com.programmers.film.domain.post.domain.PostDetail;
import com.programmers.film.domain.post.domain.PostImage;
import com.programmers.film.domain.post.domain.PostState;
import com.programmers.film.domain.post.domain.PostStatus;
import com.programmers.film.domain.post.repository.PostAuthorityRepository;
import com.programmers.film.domain.post.repository.PostDetailRepository;
import com.programmers.film.domain.post.repository.PostImageRepository;
import com.programmers.film.domain.post.repository.PostRepository;
import com.programmers.film.domain.post.repository.PostStateRepository;
import com.programmers.film.domain.user.domain.User;
import com.programmers.film.domain.user.repository.UserRepository;
import com.programmers.film.img.S3Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostAuthorityRepository authorityRepository;
    private final PostRepository postRepository;
    private final PostDetailRepository postDetailRepository;
    private final PostImageRepository postImageRepository;
    private final PostConverter postConverter;
    private final UserRepository userRepository;
    private final PostStateRepository postStateRepository;
    private final S3Service s3Service;

    @Transactional
    public CreatePostResponse createPost(CreatePostRequest request, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserIdNotFoundExceoption("사용자 ID를 찾을 수 없습니다. 게시물을 생성할 수 없습니다."));
        Post draftPost = postConverter.createPostRequestToPost(request, user);
        Post post = postRepository.save(draftPost);

        // TODO : must have 권한, 이미지 1개 , exception
        PostAuthority authority = new PostAuthority();
        post.addPostAuthority(authority);
        user.addAuthority(authority);
        authorityRepository.save(authority);

        // img upload
        List<String> ImgUrls = request.getImageFiles().stream()
            .map(orderImageFileDto -> {
                try {
                    return s3Service.upload(orderImageFileDto.getImage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            })
            .toList();

        PostImage postImage = null;
        try {
            String ImgUrl = ImgUrls.get(0);
            PostImage draftPostImage = PostImage.builder()
                .imageUrl(
                    ImageUrl.builder()
                        .originalSizeUrl(ImgUrl)
                        .build()
                )
                .build();
            postImage = postImageRepository.save(draftPostImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PostDetail draftPostDetail = PostDetail.builder()
            .id(post.getId())
            .post(post)
            .content(request.getContent())
            .build();
        PostDetail postDetail = postDetailRepository.save(draftPostDetail);

        if(postImage != null) {
            postDetail.addPostImage(postImage);
        }

        return postConverter.postToCreatePostResponse(post);
    }

    @Transactional(readOnly = true)
    public PreviewPostResponse getPreview(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostIdNotFoundException("게시물을 찾을 수 없습니다. 게시물 엿보기를 할 수 없습니다."));
        if(post.getIsDeleted() == 0) {
            throw new PostIdNotFoundException("삭제된 게시물입니다. 게시물 엿보기를 할 수 없습니다.");
        }
        return postConverter.postToPreviewPostResponse(post);
    }

    @Transactional
    public DeletePostResponse removePost(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostIdNotFoundException("게시물을 찾을 수 없습니다. 게시물 삭제를 할 수 없습니다."));
        return postConverter.postToDeletePostResponse(post.removePost());
    }

    @Transactional
    public GetPostDetailResponse getPostDetail(Long postId, Long userId){
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserIdNotFoundExceoption("잘못된 유저 입니다.")); // TODO : 상황발생시 문구 수정

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostIdNotFoundException("게시물을 찾을 수 없습니다. 게시물 확인을 할 수 없습니다."));

        if(post.getIsDeleted() == 0) {
            throw new PostIdNotFoundException("삭제된 게시물입니다. 게시물 확인을 할 수 없습니다.");
        }

        PostDetail postDetail = postDetailRepository.findByPostId(postId)
            .orElseThrow(() -> new PostIdNotFoundException("게시물 세부 내용을 찾을 수 없습니다. 게시물 확인을 할 수 없습니다."));

        if(post.getState().equals(PostStatus.OPENABLE.toString())){
            PostState state = postStateRepository.findByState(PostStatus.OPENED.toString()).get();
            post.setState(state);
            postDetail.firstOpen(user);
        }

        else if(post.getState().equals(PostStatus.CLOSED.toString())){
            throw new PostCanNotOpenException("닫혀 있는 게시물 입니다.");
        }

        return postConverter.postToGetPostDetailResponse(post,postDetail);
    }


}
