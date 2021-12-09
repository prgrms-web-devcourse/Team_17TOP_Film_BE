package com.programmers.film.api.config;

import com.programmers.film.api.auth.jwt.Jwt;
import com.programmers.film.api.auth.jwt.JwtAuthenticationFilter;
import com.programmers.film.api.auth.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.programmers.film.api.auth.oauth2.OAuth2AuthenticationFailureHandler;
import com.programmers.film.api.auth.oauth2.OAuth2AuthenticationSuccessHandler;
import com.programmers.film.api.auth.service.AuthService;
import com.programmers.film.api.config.properties.AppProperties;
import com.programmers.film.api.config.properties.CorsProperties;
import com.programmers.film.api.config.properties.JwtProperties;
import java.util.Arrays;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final JwtProperties jwtProperties;
	private final AppProperties appProperties;

	private final AuthService authService;

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

			// 	=========================test 주석 ======================
//				.antMatchers("/api/**").hasAnyRole("USER")
//				.anyRequest().authenticated()


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
			.userInfoEndpoint()
				.userService(authService)
				.and()
			.successHandler(oauth2AuthenticationSuccessHandler())
				.authorizedClientRepository(
					getApplicationContext().getBean(AuthenticatedPrincipalOAuth2AuthorizedClientRepository.class)
				)
			.failureHandler(oAuth2AuthenticationFailureHandler())
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
			jwtProperties.getIssuer(),
			jwtProperties.getClientSecret(),
			jwtProperties.getExpirySeconds()
		);
	}

	@Bean
	public HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository() {
		return new HttpCookieOAuth2AuthorizationRequestRepository();
	}

	@Bean
	public OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler() {
		return new OAuth2AuthenticationSuccessHandler(
			getApplicationContext().getBean(JdbcOAuth2AuthorizedClientService.class),
			authorizationRequestRepository(),
			appProperties,
			authService,
			getApplicationContext().getBean(Jwt.class));
	}

	@Bean
	public OAuth2AuthorizedClientService authorizedClientService(
		JdbcOperations jdbcOperations,
		ClientRegistrationRepository clientRegistrationRepository
	) {
		return new JdbcOAuth2AuthorizedClientService(jdbcOperations, clientRegistrationRepository);
	}

	@Bean
	public OAuth2AuthorizedClientRepository authorizedClientRepository(
		OAuth2AuthorizedClientService authorizedClientService) {
		return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService);
	}

	@Bean
	public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
		return new OAuth2AuthenticationFailureHandler(authorizationRequestRepository());
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
		return new JwtAuthenticationFilter(jwt);
	}

	@Bean
	public UrlBasedCorsConfigurationSource corsConfigurationSource(CorsProperties corsProperties) {
		UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();

		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
		corsConfig.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
		corsConfig.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
		corsConfig.setAllowCredentials(true);
		corsConfig.setMaxAge(corsConfig.getMaxAge());

		corsConfigSource.registerCorsConfiguration("/**", corsConfig);
		return corsConfigSource;
	}
}
