package com.programmers.film.api.post.service;

import com.programmers.film.api.post.converter.PostConverter;
import com.programmers.film.api.post.dto.request.CreatePostRequest;
import com.programmers.film.api.post.dto.request.FixPostAuthorityRequest;
import com.programmers.film.api.post.dto.response.CreatePostResponse;
import com.programmers.film.api.post.dto.response.DeletePostResponse;
import com.programmers.film.api.post.dto.response.FixPostAuthorityResponse;
import com.programmers.film.api.post.dto.response.GetPostDetailResponse;
import com.programmers.film.api.post.dto.response.PreviewPostResponse;
import com.programmers.film.api.post.exception.PostAuthorityException;
import com.programmers.film.api.post.exception.PostIdNotFoundException;
import com.programmers.film.api.post.exception.PostCanNotOpenException;
import com.programmers.film.api.post.util.PostValidateUtil;
import com.programmers.film.api.user.exception.UserIdNotFoundException;
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
import java.util.List;
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
    private final PostValidateUtil validateUtil;

    @Transactional
    public CreatePostResponse createPost(CreatePostRequest request, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserIdNotFoundException("사용자 ID를 찾을 수 없습니다. 게시물을 생성할 수 없습니다."));
        Post draftPost = postConverter.createPostRequestToPost(request, user);
        Post post = postRepository.save(draftPost);

        // TODO : must have 권한, 이미지 1개 , exception
        PostAuthority authority = new PostAuthority();
        post.addPostAuthority(authority);
        user.addAuthority(authority);
        authorityRepository.save(authority);

        PostDetail draftPostDetail = PostDetail.builder()
            .id(post.getId())
            .post(post)
            .content(request.getContent())
            .build();
        PostDetail postDetail = postDetailRepository.save(draftPostDetail);

        // img upload
        PostImage postImage = null;
        try {
            if (request.getImageFiles() != null) {
                String ImgUrl = request.getImageFiles().get(0).getUrl();
                PostImage draftPostImage = PostImage.builder()
                    .imageUrl(
                        ImageUrl.builder()
                            .originalSizeUrl(ImgUrl)
                            .build()
                    )
                    .postDetail(postDetail)
                    .build();
                postImage = postImageRepository.save(draftPostImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return postConverter.postToCreatePostResponse(post);
    }

    @Transactional(readOnly = true)
    public PreviewPostResponse getPreview(Long postId, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserIdNotFoundException("잘못된 유저 입니다. 게시물 엿보기를 할 수 없습니다."));
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostIdNotFoundException("게시물을 찾을 수 없습니다. 게시물 엿보기를 할 수 없습니다."));

        if(validateUtil.checkIsDelete(post)) {
            throw new PostCanNotOpenException("삭제된 게시물입니다. 게시물을 게시물 엿보기를 할 수 없습니다.");
        }
        if(validateUtil.checkAuthority(post, user)){
            throw new PostCanNotOpenException("열람 권한이 없는 게시물입니다. 게시물을 게시물 엿보기를 할 수 없습니다.");
        }

        return postConverter.postToPreviewPostResponse(post);
    }

    @Transactional
    public DeletePostResponse removePost(Long postId, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserIdNotFoundException("잘못된 유저 입니다. 게시물 엿보기를 할 수 없습니다."));
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostIdNotFoundException("게시물을 찾을 수 없습니다. 게시물 삭제를 할 수 없습니다."));

        if(validateUtil.checkIsDelete(post)) {
            throw new PostCanNotOpenException("이미 삭제된 게시물입니다. 게시물 삭제를 할 수 없습니다.");
        }

        User getAuthor = post.getAuthor();
        if(!getAuthor.equals(user)) {
            throw new PostCanNotOpenException("작성자가 아닙니다. 게시물 삭제를 할 수 없습니다.");
        }

        return postConverter.postToDeletePostResponse(post.removePost());
    }

    @Transactional
    public GetPostDetailResponse getPostDetail(Long postId, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserIdNotFoundException("잘못된 유저 입니다. 게시물 확인을 할 수 없습니다."));
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostIdNotFoundException("게시물을 찾을 수 없습니다. 게시물 확인을 할 수 없습니다."));

        if(validateUtil.checkIsDelete(post)) {
            throw new PostCanNotOpenException("삭제된 게시물입니다. 게시물 확인을 할 수 없습니다.");
        }
        if(validateUtil.checkAuthority(post, user)){
            throw new PostCanNotOpenException("열람 권한이 없는 게시물입니다. 게시물 확인을 할 수 없습니다.");
        }

        PostDetail postDetail = postDetailRepository.findByPostId(postId)
            .orElseThrow(
                () -> new PostIdNotFoundException("게시물 세부 내용을 찾을 수 없습니다. 게시물 확인을 할 수 없습니다."));

        PostState postState = post.getState();
        if (postState.toString().equals(PostStatus.CLOSED.toString())) {
            throw new PostCanNotOpenException("닫혀 있는 게시물 입니다. 게시물을 확인을 할 수 없습니다.");
        } else if (postState.toString().equals(PostStatus.OPENABLE.toString())) {
            PostState state = postStateRepository.findByPostStateValue(PostStatus.OPENED.toString())
                .get();
            post.setState(state);
            postDetail.firstOpen(user);
            return postConverter.postToGetPostDetailResponse(post.getId(), userId);
        }

        return postConverter.postToGetPostDetailResponse(post.getId());
    }

    public FixPostAuthorityResponse fixPostAuthority(FixPostAuthorityRequest request, Long postId, Long userId) {
        runFixPostAuthority(request, postId, userId);
        return getPostAuthority(postId);
    }

    @Transactional
    public void runFixPostAuthority(FixPostAuthorityRequest request, Long postId, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserIdNotFoundException("잘못된 유저 입니다. 열람권한을 수정할 수 없습니다."));
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostIdNotFoundException("게시물을 찾을 수 없습니다. 열람권한을 수정할 수 없습니다."));

        if(validateUtil.checkIsDelete(post)) {
            throw new PostCanNotOpenException("삭제된 게시물입니다. 열람권한을 수정할 수 없습니다.");
        }
        if(validateUtil.checkAuthority(post, user)){
            throw new PostCanNotOpenException("열람 권한이 없는 게시물입니다. 열람권한을 수정할 수 없습니다.");
        }

        User author = userRepository.findById(post.getAuthor().getId())
            .orElseThrow(() -> new PostIdNotFoundException("작성자를 찾을 수 없습니다. 열람권한을 수정할 수 없습니다."));
        if(!author.equals(user)) {
            throw new PostAuthorityException("작성자만 열람권한을 수정할 수 있습니다. 열람권한을 수정할 수 없습니다.");
        }

        List<PostAuthority> postAuthorityList = authorityRepository.findByPostId(postId);
        // delete all saved data
        postAuthorityList.forEach(
            postAuthority -> {
                User getUser = userRepository.findById(postAuthority.getUser().getId())
                    .orElseThrow(() -> new UserIdNotFoundException("저장 오류. 게시물 열람권한 목록을 불러오는데 실패했습니다."));

                // delete except post author
                if(!author.equals(getUser)) {
                    authorityRepository.deleteById(postAuthority.getId());
                }
            }
        );

        // save request data
        request.getFixAuthorityList()
            .forEach(simpleFixAuthorityDto -> {
                    User getUser = userRepository.findById(simpleFixAuthorityDto.getUserId())
                        .orElseThrow(() -> new UserIdNotFoundException("잘못된 사용자ID 입니다. 열람권한을 수정할 수 없습니다." +
                            simpleFixAuthorityDto.getUserId()));

                    if(author.equals(getUser)) {
                        throw new PostAuthorityException("게시물 작성자의 열람권한은 수정할 수 없습니다.");
                    }

                    // add post authority to get user
                    PostAuthority postAuthority = new PostAuthority();
                    post.addPostAuthority(postAuthority);
                    getUser.addAuthority(postAuthority);
                    authorityRepository.save(postAuthority);
                }
            );
    }

    @Transactional(readOnly = true)
    public FixPostAuthorityResponse getPostAuthority(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostIdNotFoundException("게시물을 찾을 수 없습니다. 열람권한 불러올 수 없습니다."));
        return postConverter.postToFixPostAuthorityResponse(post.getId());
    }
}
