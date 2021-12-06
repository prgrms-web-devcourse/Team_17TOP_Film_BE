package com.programmers.film.api.auth.controller;

import com.programmers.film.api.auth.dto.request.LoginRequest;
import com.programmers.film.api.auth.dto.response.LoginResponse;
import com.programmers.film.api.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

	private final AuthService authService;

//	@PostMapping(value = "/join")
//	public JoinResponse join(JoinRequest joinRequest) {
//		return authService.join()
//		return null;
//	}
}
