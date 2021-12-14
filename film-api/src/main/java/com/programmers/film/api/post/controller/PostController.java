package com.programmers.film.api.post.controller;

import com.google.gson.Gson;
import com.programmers.film.api.config.interceptor.Auth;
import com.programmers.film.api.config.resolver.UserId;
import com.programmers.film.api.post.dto.request.CreatePostRequest;
import com.programmers.film.api.post.dto.request.CreatePostRequestString;
import com.programmers.film.api.post.dto.response.CreatePostResponse;
import com.programmers.film.api.post.dto.response.DeletePostResponse;
import com.programmers.film.api.post.dto.response.GetPostDetailResponse;
import com.programmers.film.api.post.dto.response.PreviewPostResponse;
import com.programmers.film.api.post.service.PostService;
import com.programmers.film.img.S3Service;
import java.io.IOException;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final S3Service s3Service;

    @Auth
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<CreatePostResponse> createPost(
        @RequestPart(value = "files", required = false) List<MultipartFile> files,
        @Valid @RequestPart("com") String str,
        @UserId Long userId) throws IOException {

        Gson gson = new Gson();
        CreatePostRequestString requestString = gson.fromJson(str, CreatePostRequestString.class);

        CreatePostRequest request = CreatePostRequest.builder().build();
        request.mappingString(requestString);

        if (files != null) {
            request.setImageFiles(s3Service.upload(files));
        }
        CreatePostResponse response = postService.createPost(request, userId);
        return ResponseEntity.created(URI.create("/api/v1/posts/" + response.getPostId()))
            .body(response);
    }

    @Auth
    @GetMapping("/{postId}")
    public ResponseEntity<PreviewPostResponse> previewPost(@PathVariable("postId") Long postId,
        @UserId Long userId) {
        PreviewPostResponse preview = postService.getPreview(postId, userId);
        return ResponseEntity.ok(preview);
    }

    @Auth
    @DeleteMapping("/{postId}")
    public ResponseEntity<DeletePostResponse> deletePost(@PathVariable("postId") Long postId,
        @UserId Long userId) {
        DeletePostResponse deletePostResponse = postService.removePost(postId, userId);
        return ResponseEntity.ok(deletePostResponse);
    }

    @Auth
    @GetMapping("/detail/{postId}")
    public ResponseEntity<GetPostDetailResponse> getPostDetail(@PathVariable("postId") Long postId,
        @UserId Long userId) {
        GetPostDetailResponse response = postService.getPostDetail(postId, userId);
        return ResponseEntity.ok(response);
    }
}
