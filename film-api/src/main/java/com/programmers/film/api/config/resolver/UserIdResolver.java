package com.programmers.film.api.config.resolver;

import com.programmers.film.common.error.exception.InternalServerErrorException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class UserIdResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAnnotation = parameter.getParameterAnnotation(UserId.class) != null;
        boolean isMatchType = parameter.getParameterType().equals(Long.class);
        return hasAnnotation && isMatchType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (parameter.getMethodAnnotation(UserId.class) == null) {
            throw new InternalServerErrorException("It requires authentication.");
        }

        return webRequest.getAttribute("user_id", RequestAttributes.SCOPE_REQUEST);
    }
}
