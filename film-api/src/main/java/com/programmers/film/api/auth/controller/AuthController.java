package com.programmers.film.api.auth.controller;

import com.programmers.film.api.auth.dto.LoginRequest;
import com.programmers.film.api.auth.dto.LoginResponse;
import com.programmers.film.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

	private final UserService userService;

	@GetMapping(value = "/login")
	public LoginResponse login(@AuthenticationPrincipal LoginRequest loginRequest) {
		return userService.findByUsername(loginRequest.username)
			.map(user ->
				new LoginResponse(loginRequest.token, loginRequest.username,
					user.getGroup().getName())
			)
			.orElseThrow(() -> new IllegalArgumentException(
				"Could not found user for " + loginRequest.username));
	}
}
