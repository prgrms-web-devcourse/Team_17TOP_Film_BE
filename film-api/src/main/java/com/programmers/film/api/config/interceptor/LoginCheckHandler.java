package com.programmers.film.api.config.interceptor;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginCheckHandler {

    private final OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository;

    public Long getUserId(HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        authentication.getDetails();

//        if (isNotEmpty(accessToken)) {
//            Session session = findSessionBySessionId(sessionId);
//            Long userId = session.getAttribute(USER_ID);
//            if (userId != null) {
//                return userId;
//            }
//        }
        return null;
    }
}
