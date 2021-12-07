package com.programmers.film.api.post.controller;

import com.programmers.film.api.post.dto.request.CreatePostRequest;
import com.programmers.film.api.post.dto.response.CreatePostResponse;
import com.programmers.film.api.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

//    @Auth
//    @PostMapping("/post")
//    public ResponseEntity<CreatePostResponse> createPost(@RequestBody CreatePostRequest request, @Userid userId){
//        CreatePostResponse response = postService.createPost(request,userId);
//        return ResponseEntity.ok(response);
//    }

}
