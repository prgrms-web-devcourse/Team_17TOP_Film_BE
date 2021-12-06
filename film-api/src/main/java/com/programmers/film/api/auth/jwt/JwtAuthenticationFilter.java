package com.programmers.film.api.auth.jwt;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import com.programmers.film.api.auth.dto.request.LoginRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final String headerKey;

	private final Jwt jwt;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
		throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			String token = getToken(request);
			if (token != null) {
				try {
					Jwt.Claims claims = verify(token);
					log.debug("Jwt parse result: {}", claims);

					String username = claims.username;
					List<GrantedAuthority> authorities = getAuthorities(claims);

					if (isNotEmpty(username) && authorities.size() > 0) {
						JwtAuthenticationToken authentication =
							new JwtAuthenticationToken(new LoginRequest(token, username), null,
								authorities);
						authentication.setDetails(
							new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				} catch (Exception e) {
					log.warn("Jwt processing failed: {}", e.getMessage());
				}
			}
		} else {
			log.debug(
				"SecurityContextHolder not populated with security token, as it already contained: '{}'",
				SecurityContextHolder.getContext().getAuthentication());
		}

		chain.doFilter(request, response);
	}

	private String getToken(HttpServletRequest request) {
		String token = request.getHeader(headerKey);
		if (isNotEmpty(token)) {
			log.debug("Jwt authorization api detected: {}", token);
			return URLDecoder.decode(token, StandardCharsets.UTF_8);
		}
		return null;
	}

	private Jwt.Claims verify(String token) {
		return jwt.verify(token);
	}

	private List<GrantedAuthority> getAuthorities(Jwt.Claims claims) {
		String[] roles = claims.roles;
		return roles == null || roles.length == 0 ?
			emptyList() :
			Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(toList());
	}

}
