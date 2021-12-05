package com.programmers.film.api.auth.oauth2;

import static com.programmers.film.api.auth.util.CookieUtil.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME;

import com.programmers.film.api.auth.util.CookieUtil;
import java.util.Base64;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public class HttpCookieOAuth2AuthorizationRequestRepository implements
	AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

	private final String cookieName;

	private final int cookieExpireSeconds;

	public HttpCookieOAuth2AuthorizationRequestRepository() {
		this(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, 180);
	}

	public HttpCookieOAuth2AuthorizationRequestRepository(String cookieName,
		int cookieExpireSeconds) {
		this.cookieName = cookieName;
		this.cookieExpireSeconds = cookieExpireSeconds;
	}

	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		return CookieUtil.getCookie(request, cookieName)
			.map(this::getOAuth2AuthorizationRequest)
			.orElse(null);
	}

	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest,
		HttpServletRequest request, HttpServletResponse response) {
		if (authorizationRequest == null) {
			CookieUtil.getCookie(request, cookieName)
				.ifPresent(cookie -> CookieUtil.clearCookie(cookie, response));
		} else {
			String value = Base64.getUrlEncoder()
				.encodeToString(SerializationUtils.serialize(authorizationRequest));
			Cookie cookie = new Cookie(cookieName, value);
			cookie.setPath("/");
			cookie.setHttpOnly(true);
			cookie.setMaxAge(cookieExpireSeconds);
			response.addCookie(cookie);
		}
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
		return loadAuthorizationRequest(request);
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
		HttpServletResponse response) {
		return CookieUtil.getCookie(request, cookieName)
			.map(cookie -> {
				OAuth2AuthorizationRequest oauth2Request = getOAuth2AuthorizationRequest(cookie);
				CookieUtil.clearCookie(cookie, response);
				return oauth2Request;
			})
			.orElse(null);
	}

	private OAuth2AuthorizationRequest getOAuth2AuthorizationRequest(Cookie cookie) {
		return SerializationUtils.deserialize(
			Base64.getUrlDecoder().decode(cookie.getValue())
		);
	}
}