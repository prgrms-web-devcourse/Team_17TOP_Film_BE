package com.programmers.film.api.post.controller;

import com.programmers.film.api.post.dto.response.DeletePostResponse;
import com.programmers.film.api.post.dto.response.PreviewPostResponse;
import com.programmers.film.api.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

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
}
