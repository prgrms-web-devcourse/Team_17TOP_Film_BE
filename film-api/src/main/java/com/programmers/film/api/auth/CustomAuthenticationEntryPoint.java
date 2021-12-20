package com.programmers.film.api.auth;

import com.programmers.film.common.error.ErrorCode;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException {

        log.info("Responding with unauthorized error. Message: {}", authException.getMessage());

        ErrorCode errorCode = (ErrorCode) request.getAttribute("exception");

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println(
            "{ \"message\" : \"" + errorCode.getMessage()
            + "\", \"code\" : \"" +  errorCode.getCode()
            + "\", \"status\" : " + errorCode.getStatus()
            + ", \"errors\" : [ ] }"
        );
    }
}
