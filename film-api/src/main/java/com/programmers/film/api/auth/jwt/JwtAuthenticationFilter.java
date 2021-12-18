package com.programmers.film.api.auth.jwt;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import com.programmers.film.api.auth.dto.request.JwtRequest;
import com.programmers.film.api.auth.exception.EmptyTokenException;
import com.programmers.film.api.auth.exception.InvalidTokenException;
import com.programmers.film.api.auth.util.HeaderUtil;
import com.programmers.film.common.error.ErrorCode;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Jwt jwt;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain chain)
		throws IOException, ServletException {

		// If not authenticated user
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			String token = getToken(request);
			try {
				if (token == null) throw new EmptyTokenException("Empty Token");

				Jwt.Claims claims = jwt.verify(token);
				log.debug("Jwt parse result: {}", claims);

				List<GrantedAuthority> authorities = getAuthorities(claims);
				String provider = claims.provider;
				String providerId = claims.providerId;

				if (isNotEmpty(provider) && isNotEmpty(providerId) && authorities.size() > 0) {
					JwtAuthenticationToken authentication =
						new JwtAuthenticationToken(
							new JwtRequest(token, provider, providerId),
							null,
							authorities
						);
					authentication.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			} catch (EmptyTokenException e) {
				log.warn("Jwt processing failed: {}", e.getMessage());
				request.setAttribute("exception", ErrorCode.EMPTY_TOKEN);
			} catch (Exception e) {
				log.warn("Jwt processing failed: {}", e.getMessage());
				request.setAttribute("exception", ErrorCode.INVALID_TOKEN);
			}
		} else {
			log.debug(
				"SecurityContextHolder not populated with security token, as it already contained: '{}'",
				SecurityContextHolder.getContext().getAuthentication());
		}

		chain.doFilter(request, response);
	}

	private String getToken(HttpServletRequest request) {
		String token = HeaderUtil.getAccessToken(request);
		if (isNotEmpty(token)) {
			log.debug("Jwt authorization api detected: {}", token);
			return URLDecoder.decode(token, StandardCharsets.UTF_8);
		}
		return null;
	}

	private List<GrantedAuthority> getAuthorities(Jwt.Claims claims) {
		String[] roles = claims.roles;
		return roles == null || roles.length == 0 ?
			emptyList() :
			Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(toList());
	}

}
