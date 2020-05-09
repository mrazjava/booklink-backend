package com.github.mrazjava.booklink.security;

import org.slf4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.mrazjava.booklink.security.AccessTokenSecurityFilter.ATTR_AUTH_TOKEN_STATUS;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

/**
 * Handler for requests which require authentication. Not invoked on endpoints configured
 * for public access.
 *
 * By explicitly throwing a HTTP 401, we prevent default boot mechanism of redirect to a
 * login page. Since we provide restful api, we want to return an error so that whatever
 * front end UI connects to it can decide if (and how) it wants to redirect.
 *
 * @author AZ
 * @see com.github.mrazjava.booklink.config.SecurityConfiguration
 */
@Component
public class InvalidAuthTokenEntryPoint implements AuthenticationEntryPoint {

    @Inject @Named("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    /**
     * Invoked automatically by boot when authentication has failed. In our case failed authentication means
     * invalid token. Arguments represent error state. See interface this entry point implements for more
     * documentation. Since this runs before dispatcher servlet, normally this exception would not be handled
     * by exception handler. Since we want it handled uniformly, we explicitly notify the resolver so that
     * it can be handled by {@link com.github.mrazjava.booklink.CustomExceptionHandler}
     *
     * @param request that failed
     * @param response to handle as a result of failed request
     * @param authException raised by boot which can be further handled
     * @see AccessTokenSecurityFilter
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        resolver.resolveException(request, response, null, new InvalidAccessTokenException("bad access token", authException));
    }
}
