package com.programmers.film.api.auth.oauth2;

import static com.programmers.film.api.auth.util.CookieUtil.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME;
import static com.programmers.film.api.auth.util.CookieUtil.REDIRECT_URI_PARAM_COOKIE_NAME;
import static com.programmers.film.api.auth.util.CookieUtil.REFRESH_TOKEN;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.programmers.film.api.auth.util.CookieUtil;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
			CookieUtil.clearCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
			CookieUtil.clearCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
			CookieUtil.clearCookie(request, response, REFRESH_TOKEN);
			return;
		}

		String value = CookieUtil.serialize(authorizationRequest);
		Cookie cookie = new Cookie(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, value);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(cookieExpireSeconds);
		response.addCookie(cookie);

		String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
		if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
			CookieUtil.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin,
				cookieExpireSeconds);
		}
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
		return loadAuthorizationRequest(request);
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
		HttpServletResponse response) {
		return this.loadAuthorizationRequest(request);
	}

	public void removeAuthorizationRequestCookies(HttpServletRequest request,
		HttpServletResponse response) {
		CookieUtil.clearCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
		CookieUtil.clearCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
		CookieUtil.clearCookie(request, response, REFRESH_TOKEN);
	}

	private OAuth2AuthorizationRequest getOAuth2AuthorizationRequest(Cookie cookie) {
		return CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class);
	}
}
