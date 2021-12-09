package com.programmers.film.api.config.resolver;

import com.programmers.film.api.auth.dto.ProviderAttribute;
import com.programmers.film.api.config.interceptor.Auth;
import com.programmers.film.common.error.exception.InternalServerErrorException;
import java.util.Map;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class ProviderResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAnnotation = parameter.getParameterAnnotation(Provider.class) != null;
        boolean isMatchType = parameter.getParameterType().equals(ProviderAttribute.class);
        return hasAnnotation && isMatchType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (parameter.getMethodAnnotation(Auth.class) == null) {
            throw new InternalServerErrorException("It requires authentication.");
        }

        return webRequest.getAttribute("provider", RequestAttributes.SCOPE_REQUEST);
    }
}
