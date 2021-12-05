package com.programmers.film.api.auth.util;

import static java.util.Optional.ofNullable;

import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.util.WebUtils;

public class CookieUtil {

	public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "OAUTH2_AUTHORIZATION_REQUEST";
	public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "REDIRECT_URL";
	public static final String REFRESH_TOKEN = "REFRESH_TOKEN";

	public static Optional<Cookie> getCookie(HttpServletRequest request, String cookieName) {
		return ofNullable(WebUtils.getCookie(request, cookieName));
	}

	public static void addCookie(HttpServletResponse response, String name, String value,
		int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(maxAge);

		response.addCookie(cookie);
	}

	public static void clearCookie(Cookie cookie, HttpServletResponse response) {
		cookie.setValue("");
		cookie.setPath("/");
		cookie.setMaxAge(0);

		response.addCookie(cookie);
	}
}
