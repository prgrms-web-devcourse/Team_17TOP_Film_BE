package com.programmers.film.api.auth.oauth2;

import com.programmers.film.api.auth.jwt.Jwt;
import com.programmers.film.api.auth.service.AuthService;
import com.programmers.film.domain.user.entity.User;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends
	SavedRequestAwareAuthenticationSuccessHandler {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Jwt jwt;

	private final AuthService authService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws ServletException, IOException {
		if (authentication instanceof OAuth2AuthenticationToken oauth2Token) {
			OAuth2User principal = oauth2Token.getPrincipal();
			String registrationId = oauth2Token.getAuthorizedClientRegistrationId();

			User user = processUserOAuth2UserJoin(principal, registrationId);
			String loginSuccessJson = generateLoginSuccessJson(user);
			response.setContentType("application/json;charset=UTF-8");
			response.setContentLength(loginSuccessJson.getBytes(StandardCharsets.UTF_8).length);
			response.getWriter().write(loginSuccessJson);
		} else {
			super.onAuthenticationSuccess(request, response, authentication);
		}
	}

	private User processUserOAuth2UserJoin(OAuth2User oAuth2User, String registrationId) {
		return authService.join(oAuth2User, registrationId);
	}

	private String generateLoginSuccessJson(User user) {
		String token = generateToken(user);
		log.debug("Jwt({}) created for oauth2 login user {}", token, user.getUsername());
		return "{\"token\":\"" + token + "\", \"username\":\"" + user.getUsername()
			+ "\", \"group\":\"" + user.getGroup().getName() + "\"}";
	}

	private String generateToken(User user) {
		return jwt.sign(Jwt.Claims.from(user.getUsername(), new String[]{"ROLE_USER"}));
	}
}
