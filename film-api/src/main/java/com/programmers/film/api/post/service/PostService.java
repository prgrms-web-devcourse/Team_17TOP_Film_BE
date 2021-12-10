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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public CreatePostResponse createPost(CreatePostRequest request, Long userId) {

        Post post = createPostMethod(request, userId);
        // TODO : must have 권한, 이미지 1개 , exception
        PostAuthority postAuthority = createAuthrityMethod(post,userId);

        PostDetail postDetail = createPostDetail(request, post);

        //System.out.println(postDetail);

        // img upload
        if(request.getImageFiles().size()!=0) {
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
        }


        System.out.println("============test========log");
        Post foundPost = postRepository.findById(post.getId()).get();
        System.out.println(foundPost.getPostAuthorities());

        return postConverter.postToCreatePostResponse(foundPost);
    }

    @Transactional
    protected PostDetail createPostDetail(CreatePostRequest request, Post findPost) {
        PostDetail postDetail = new PostDetail(request.getContent());
        postDetail.setPost(findPost);
        return postDetailRepository.save(postDetail);
    }

    @Transactional
    protected PostAuthority createAuthrityMethod(Post post,Long userId) {
        User user = userRepository.findById(userId).orElseThrow( ()->new UserIdNotFoundExceoption("잘못된 유저 입니다."));
        PostAuthority authority = new PostAuthority();
        Post foundPost = postRepository.findById(post.getId()).get();

        System.out.println("=========error");
        user.addAuthority(authority);
        foundPost.addPostAuthority(authority);

//        authority.setPost(findPost);
//        authority.setUser(user);
        System.out.println("==========user user user");
        System.out.println(authority.getUser());
        System.out.println(authority.getPost());
        return authorityRepository.save(authority);

    }

    @Transactional
    protected Post createPostMethod(CreatePostRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow( ()->new UserIdNotFoundExceoption("잘못된 유저 입니다."));
        Post post = postConverter.createPostRequestToPost(request, user);
        Post findPost = postRepository.save(post);
        return findPost;
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
