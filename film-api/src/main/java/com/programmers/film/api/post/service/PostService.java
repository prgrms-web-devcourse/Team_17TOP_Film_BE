package com.programmers.film.api.post.service;

import com.programmers.film.api.post.converter.PostConverter;
import com.programmers.film.api.post.dto.response.DeletePostResponse;
import com.programmers.film.api.post.dto.response.PreviewPostResponse;
import com.programmers.film.api.post.exception.PostIdNotFoundException;
import com.programmers.film.domain.member.repository.UserRepository;
import com.programmers.film.domain.post.domain.Post;
import com.programmers.film.domain.post.repository.PostAuthorityRepository;
import com.programmers.film.domain.post.repository.PostDetailRepository;
import com.programmers.film.domain.post.repository.PostImageRepository;
import com.programmers.film.domain.post.repository.PostRepository;
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
    private final UserRepository userRepository;
    private final PostConverter postConverter;

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
