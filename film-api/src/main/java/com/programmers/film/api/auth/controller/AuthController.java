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

//	@GetMapping("/refresh")
//	public void refreshToken (HttpServletRequest request, HttpServletResponse response) {
//
//		// check access token
//		String accessToken = HeaderUtil.getAccessToken(request);
//		Jwt.Claims claims = jwt.verify(accessToken);
//		if (claims.checkExpired()) {
//			// expired token
//			return;
//		}
//
//		String userId = claims.getSubject();
//		RoleType roleType = RoleType.of(claims.get("role", String.class));
//
//		// refresh token
//		String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN_COOKIE_NAME)
//			.map(Cookie::getValue)
//			.orElse((null));
//		AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);
//
//		if (authRefreshToken.validate()) {
//			return ApiResponse.invalidRefreshToken();
//		}
//
//		// userId refresh token 으로 DB 확인
//		UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken);
//		if (userRefreshToken == null) {
//			return ApiResponse.invalidRefreshToken();
//		}
//
//		Date now = new Date();
//		AuthToken newAccessToken = tokenProvider.createAuthToken(
//			userId,
//			roleType.getCode(),
//			new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
//		);
//
//		long validTime = authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();
//
//		// refresh 토큰 기간이 3일 이하로 남은 경우, refresh 토큰 갱신
//		if (validTime <= THREE_DAYS_MSEC) {
//			// refresh 토큰 설정
//			long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
//
//			authRefreshToken = tokenProvider.createAuthToken(
//				appProperties.getAuth().getTokenSecret(),
//				new Date(now.getTime() + refreshTokenExpiry)
//			);
//
//			// DB에 refresh 토큰 업데이트
//			userRefreshToken.setRefreshToken(authRefreshToken.getToken());
//
//			int cookieMaxAge = (int) refreshTokenExpiry / 60;
//			CookieUtil.clearCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
//			CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, authRefreshToken.getToken(), cookieMaxAge);
//		}
//
//		return ApiResponse.success("token", newAccessToken.getToken());
//	}
}
