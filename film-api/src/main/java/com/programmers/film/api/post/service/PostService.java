package com.programmers.film.api.post.service;

import com.programmers.film.api.post.dto.request.CreatePostRequest;
import com.programmers.film.api.post.dto.response.CreatePostResponse;
import com.programmers.film.domain.member.repository.UserRepository;
import com.programmers.film.domain.post.repository.AuthorityRepository;
import com.programmers.film.domain.post.repository.PostDetailRepository;
import com.programmers.film.domain.post.repository.PostImageRepository;
import com.programmers.film.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final AuthorityRepository authorityRepository; // 권한 서비스따로 빼는게 맞겠죠
    private final PostRepository postRepository;
    private final PostDetailRepository postDetailRepository;
    private final PostImageRepository postImageRepository;
    private final UserRepository userRepository;


    @Transactional
    public CreatePostResponse createPost(CreatePostRequest request){
    // authority , post , post_detail , postImg 생성
        authorNickname->user
        post.setter(user);


        postRepository.save()

    }

}
