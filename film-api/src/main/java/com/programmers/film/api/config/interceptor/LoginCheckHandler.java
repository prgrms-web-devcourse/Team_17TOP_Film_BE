package com.programmers.film.api.config.interceptor;

import com.programmers.film.api.auth.dto.request.JwtRequest;
import com.programmers.film.api.auth.exception.AuthNotFoundException;
import com.programmers.film.api.auth.jwt.JwtAuthenticationToken;
import com.programmers.film.domain.auth.domain.Auth;
import com.programmers.film.domain.auth.repository.AuthRepository;
import java.text.MessageFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginCheckHandler {

    private final AuthRepository authRepository;

    public Auth getAuth() {
        JwtAuthenticationToken authToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
            .getAuthentication();
        JwtRequest jwtRequest = (JwtRequest) authToken.getPrincipal();

        return authRepository.findByProviderAndProviderId(jwtRequest.provider,
                jwtRequest.providerId)
            .orElseThrow(() -> new AuthNotFoundException(
                MessageFormat.format("provider: {0}, providerId: {1}", jwtRequest.provider,
                    jwtRequest.providerId))
            );
    }
}
