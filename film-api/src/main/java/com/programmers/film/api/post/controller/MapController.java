package com.programmers.film.api.post.controller;

import com.programmers.film.api.config.interceptor.Auth;
import com.programmers.film.api.config.resolver.UserId;
import com.programmers.film.api.post.dto.response.GetMapResponse;
import com.programmers.film.api.post.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/maps")
public class MapController {
    private final MapService mapService;

    @Auth
    @GetMapping
    public ResponseEntity<GetMapResponse> getMap(@UserId Long userId) {
        GetMapResponse response = mapService.getMapData(userId);
        return ResponseEntity.ok(response);
    }
}
