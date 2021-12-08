package com.programmers.film.api.post.controller;

import com.programmers.film.api.config.interceptor.Auth;
import com.programmers.film.api.config.resolver.UserId;
import com.programmers.film.api.post.dto.request.CreatePostRequest;
import com.programmers.film.api.post.dto.response.CreatePostResponse;
import com.programmers.film.api.post.dto.response.DeletePostResponse;
import com.programmers.film.api.post.dto.response.GetPostDetailResponse;
import com.programmers.film.api.post.dto.response.PreviewPostResponse;
import com.programmers.film.api.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @Auth
    @PostMapping
    public ResponseEntity<CreatePostResponse> createPost(@RequestBody CreatePostRequest request, @UserId Long userId){
        CreatePostResponse response = postService.createPost(request,userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PreviewPostResponse> previewPost(@PathVariable("postId") Long postId) {
        PreviewPostResponse preview = postService.getPreview(postId);
        return ResponseEntity.ok(preview);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<DeletePostResponse> deletePost(@PathVariable("postId") Long postId) {
        DeletePostResponse deletePostResponse = postService.deletePost(postId);
        return ResponseEntity.ok(deletePostResponse);
    }

    @GetMapping("/detail/{postId}")
    public ResponseEntity<GetPostDetailResponse> getPostDetail(@PathVariable("postId") Long postId){
        GetPostDetailResponse response = postService.getPostDetail(postId);
        return ResponseEntity.ok(response);
    }
}
