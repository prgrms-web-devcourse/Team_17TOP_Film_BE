package com.programmers.film.api.post.service;

import com.programmers.film.api.post.converter.PostConverter;
import com.programmers.film.api.post.dto.request.CreatePostRequest;
import com.programmers.film.api.post.dto.response.CreatePostResponse;
import com.programmers.film.api.post.dto.response.DeletePostResponse;
import com.programmers.film.api.post.dto.response.PreviewPostResponse;
import com.programmers.film.api.post.exception.PostIdNotFoundException;
import com.programmers.film.domain.common.domain.ImageUrl;
import com.programmers.film.domain.member.repository.UserRepository;
import com.programmers.film.domain.post.domain.Post;
import com.programmers.film.domain.post.domain.PostAuthority;
import com.programmers.film.domain.post.domain.PostDetail;
import com.programmers.film.domain.post.domain.PostImage;
import com.programmers.film.domain.post.repository.PostAuthorityRepository;
import com.programmers.film.domain.post.repository.PostDetailRepository;
import com.programmers.film.domain.post.repository.PostImageRepository;
import com.programmers.film.domain.post.repository.PostRepository;
import com.programmers.film.img.S3Service;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostAuthorityRepository authorityRepository;
    private final PostRepository postRepository;
    private final PostDetailRepository postDetailRepository;
    private final PostImageRepository postImageRepository;
    private final PostConverter postConverter;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Transactional
    public CreatePostResponse createPost(CreatePostRequest request, Long userId) {
        Post post = postConverter.createPostRequestToPost(request);
        postRepository.save(post);

        // TODO : must have 권한, 이미지 1개 , exception
        PostAuthority authority = new PostAuthority();
        authority.setPost(post);
        authority.setUser(userRepository.findById(request.getAuthorUserId()).get());
        authorityRepository.save(authority);

        PostDetail postDetail = PostDetail.builder()
            .id(post.getId())
            .post(post)
            .content(request.getContent())
            .build();
        postDetailRepository.save(postDetail);

        // img upload
        String ImgUrl = new String();
        try {
            ImgUrl = s3Service.upload(request.getImageFiles().get(0).getImage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageUrl imageUrl = ImageUrl.builder()
            .originalSizeUrl(ImgUrl)
            .build();

        PostImage postImage = PostImage.builder()
            .imageUrl(imageUrl)
            .build();
        postImage.setPostDetail(postDetail);
        postImageRepository.save(postImage);

        return postConverter.postToCreatePostResponse(post);
    }

    @Transactional(readOnly = true)
    public PreviewPostResponse getPreview(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostIdNotFoundException("게시물을 찾을 수 없습니다. 게시물 엿보기를 할 수 없습니다."));

        return postConverter.PostToPreviewPostResponse(post);
    }

    @Transactional
    public DeletePostResponse deletePost(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostIdNotFoundException("게시물을 찾을 수 없습니다. 게시물 삭제를 할 수 없습니다."));
        post.deletePost();
        return postConverter.PostToDeletePostResponse(post);
    }

}
