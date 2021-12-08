package com.programmers.film.api.config.interceptor;

import com.programmers.film.api.auth.dto.ProviderAttribute;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final LoginCheckHandler loginCheckHandler;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
        if (auth == null) {
            return true;
        }

        com.programmers.film.domain.auth.domain.Auth authEntity = loginCheckHandler.getAuth();

        // after signup
        if (authEntity.getUser() != null) {
            Long userId = authEntity.getUser().getId();
            request.setAttribute("user_id", userId);
        }

        ProviderAttribute provider = new ProviderAttribute(
            authEntity.getProvider(),
            authEntity.getProviderId()
        );

        request.setAttribute("provider", provider);

        return true;
    }
}
