package com.programmers.film.api.post.controller;

import com.programmers.film.api.config.interceptor.Auth;
import com.programmers.film.api.config.resolver.UserId;
import com.programmers.film.api.post.dto.response.PreviewPostResponse;
import com.programmers.film.api.post.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    @Auth
    @GetMapping()
    public ResponseEntity<List<PreviewPostResponse>> previewPost(@UserId Long userId) {
        List<PreviewPostResponse> response = myPageService.getMyPosts(userId);
        return ResponseEntity.ok(response);
    }
}
