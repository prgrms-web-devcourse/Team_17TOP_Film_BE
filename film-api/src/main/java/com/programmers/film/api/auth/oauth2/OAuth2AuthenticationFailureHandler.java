package com.programmers.film.api.auth.oauth2;

import static com.programmers.film.api.auth.util.CookieUtil.REDIRECT_URI_PARAM_COOKIE_NAME;

import com.programmers.film.api.auth.util.CookieUtil;
import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private final HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception)
		throws IOException {
		String targetUrl = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
			.map(Cookie::getValue)
			.orElse(("/"));

		exception.printStackTrace();

		targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
			.queryParam("error", exception.getLocalizedMessage())
			.build().toUriString();

		authorizationRequestRepository.removeAuthorizationRequest(request, response);

		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}
