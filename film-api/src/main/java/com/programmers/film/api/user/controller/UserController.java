package com.programmers.film.api.user.controller;

import com.programmers.film.api.auth.dto.ProviderAttribute;
import com.programmers.film.api.config.interceptor.Auth;
import com.programmers.film.api.config.resolver.Provider;
import com.programmers.film.api.user.dto.request.SignUpRequest;
import com.programmers.film.api.user.dto.response.SignUpResponse;
import com.programmers.film.api.user.service.UserService;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("api/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    @Auth
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(
		@Valid @RequestBody SignUpRequest request,
        @Provider ProviderAttribute provider
	) {
        SignUpResponse response = userService.signUp(request, provider);
        return ResponseEntity.ok(response);
    }
}
