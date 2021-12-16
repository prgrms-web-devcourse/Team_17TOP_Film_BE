package com.programmers.film.api.user.controller;

import com.programmers.film.api.auth.dto.ProviderAttribute;
import com.programmers.film.api.config.interceptor.Auth;
import com.programmers.film.api.config.resolver.Provider;
import com.programmers.film.api.config.resolver.UserId;
import com.programmers.film.api.user.dto.request.SignUpRequest;
import com.programmers.film.api.user.dto.response.CheckNicknameResponse;
import com.programmers.film.api.user.dto.response.CheckUserResponse;
import com.programmers.film.api.user.dto.response.SearchUserResponse;
import com.programmers.film.api.user.dto.response.UserResponse;
import com.programmers.film.api.user.service.UserService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    @Auth
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUser(@UserId final Long userId) {
        UserResponse response = userService.getUser(userId);
        return ResponseEntity.ok(response);
    }

    @Auth
    @GetMapping
    public ResponseEntity<List<SearchUserResponse>> getUsers(
        @RequestParam(value = "keyword") String keyword,
        @RequestParam(value = "lastNickname") String lastNickname,
        @RequestParam(value = "size") int size
    ) {
        List<SearchUserResponse> responses = userService.getUsersByKeyword(keyword, lastNickname, size);
        return ResponseEntity.ok(responses);
    }

    @Auth
    @GetMapping("/duplicate")
    public ResponseEntity<CheckUserResponse> checkUserDuplicate(
        @Provider final ProviderAttribute provider) {
        return ResponseEntity.ok(userService.checkUser(provider));
    }

    @GetMapping("/{nickname}")
    public ResponseEntity<CheckNicknameResponse> checkNicknameDuplicate(
        @PathVariable final String nickname
    ) {
        CheckNicknameResponse response = userService.checkNickname(nickname);
        return ResponseEntity.ok(response);
    }

    @Auth
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(
        @Valid @RequestBody final SignUpRequest request,
        @Provider final ProviderAttribute provider
    ) {
        UserResponse response = userService.signUp(request, provider);
        return ResponseEntity.ok(response);
    }
}
