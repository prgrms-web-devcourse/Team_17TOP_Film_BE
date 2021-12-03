package com.programmers.film.api.config;

import com.programmers.film.api.auth.jwt.Jwt;
import com.programmers.film.api.auth.jwt.JwtAuthenticationFilter;
import com.programmers.film.api.auth.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.programmers.film.api.auth.oauth2.OAuth2AuthenticationSuccessHandler;
import com.programmers.film.api.auth.service.AuthService;
import java.util.Arrays;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final CorsConfigure corsConfigure;
	private final JwtConfigure jwtConfigure;

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/h2-console/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//@formatter:off
		http
			.cors()
			.and()
			.authorizeRequests()
			.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
			.antMatchers("/api/**").hasAnyRole("USER")
			.anyRequest().authenticated()
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.formLogin().disable()
			.csrf().disable()
			.headers().disable()
			.httpBasic().disable()
			.rememberMe().disable()
			.logout().disable()
			.oauth2Login()
			.authorizationEndpoint()
			.baseUri("/oauth2/authorization")
			.authorizationRequestRepository(authorizationRequestRepository())
			.and()
			.redirectionEndpoint()
			.baseUri("/*/oauth2/code/*")
			.and()
			.successHandler(getOAuth2AuthenticationSuccessHandler())
			.and()
			.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler())
			.and()
			.addFilterAfter(jwtAuthenticationFilter(), SecurityContextPersistenceFilter.class);
		// @formatter:on
	}

	@Bean
	public Jwt jwt() {
		return new Jwt(
			jwtConfigure.getIssuer(),
			jwtConfigure.getClientSecret(),
			jwtConfigure.getExpirySeconds()
		);
	}

	@Bean
	public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
		return new HttpCookieOAuth2AuthorizationRequestRepository();
	}

	@Bean
	public OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler(Jwt jwt,
		AuthService authService) {
		return new OAuth2AuthenticationSuccessHandler(jwt, authService);
	}

	public OAuth2AuthenticationSuccessHandler getOAuth2AuthenticationSuccessHandler() {
		return getApplicationContext().getBean(OAuth2AuthenticationSuccessHandler.class);
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return (request, response, e) -> {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Object principal = authentication != null ? authentication.getPrincipal() : null;
			log.warn("{} is denied", principal, e);
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType("text/plain;charset=UTF-8");
			response.getWriter().write("ACCESS DENIED");
			response.getWriter().flush();
			response.getWriter().close();
		};
	}

	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		Jwt jwt = getApplicationContext().getBean(Jwt.class);
		return new JwtAuthenticationFilter(jwtConfigure.getHeader(), jwt);
	}

	@Bean
	public UrlBasedCorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();

		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedHeaders(Arrays.asList(corsConfigure.getAllowedHeaders().split(",")));
		corsConfig.setAllowedMethods(Arrays.asList(corsConfigure.getAllowedMethods().split(",")));
		corsConfig.setAllowedOrigins(Arrays.asList(corsConfigure.getAllowedOrigins().split(",")));
		corsConfig.setAllowCredentials(true);
		corsConfig.setMaxAge(corsConfig.getMaxAge());

		corsConfigSource.registerCorsConfiguration("/**", corsConfig);
		return corsConfigSource;
	}
}
