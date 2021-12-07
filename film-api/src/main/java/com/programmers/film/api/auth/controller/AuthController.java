package com.programmers.film.api.auth.controller;

import com.programmers.film.api.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

	private final AuthService authService;

//	@Auth
//	@GetMapping(value = "/login")
//	public JoinResponse join(JoinRequest joinRequest, @UserId Long userId) {
//		return authService.join()
//		return null;
//	}
}
