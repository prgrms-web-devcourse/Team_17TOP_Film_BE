package com.programmers.film.api.auth.util;

import static java.util.Optional.ofNullable;

import java.util.Base64;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;
import org.springframework.web.util.WebUtils;

public class CookieUtil {

	public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
	public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
	public static final String REFRESH_TOKEN = "refresh_token";

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

	public static void clearCookie(HttpServletRequest request, HttpServletResponse response,
		String name) {
		Cookie[] cookies = request.getCookies();

		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					cookie.setValue("");
					cookie.setPath("/");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
	}

	public static String serialize(Object obj) {
		return Base64.getUrlEncoder()
			.encodeToString(SerializationUtils.serialize(obj));
	}

	public static <T> T deserialize(Cookie cookie, Class<T> cls) {
		return cls.cast(
			SerializationUtils.deserialize(
				Base64.getUrlDecoder().decode(cookie.getValue())
			)
		);
	}
}
