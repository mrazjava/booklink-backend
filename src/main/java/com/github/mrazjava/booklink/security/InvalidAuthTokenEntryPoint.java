package com.github.mrazjava.booklink.security;

import org.slf4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.mrazjava.booklink.security.AccessTokenSecurityFilter.ATTR_ERROR_MESSAGE;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

/**
 * Handler for requests with invalid auth token. By explicitly throwing a HTTP 401, we prevent
 * default boot mechanism of redirect to a login page. Since we provide restful api, we want to
 * return an error so that whatever front end UI connects to it can decide if (and how) it wants
 * to redirect.
 *
 * @author AZ (mrazjava)
 * @since 0.2.0
 */
@Component
public class InvalidAuthTokenEntryPoint implements AuthenticationEntryPoint {

    @Inject
    private Logger log;

    /**
     * Invoked automatically by boot when authentication has failed. In our case failed authentication means
     * invalid token. Arguments represent error state. Request is the request that failed, response is what will
     * be ultimately sent back as a result of failed auth, and exception is what boot raised up to this point
     * (that we need to handle). See implemented boot interface for more documentation.
     *
     * @see AccessTokenSecurityFilter
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        Object requestError = request.getAttribute(ATTR_ERROR_MESSAGE);
        String errorMsg = requestError == null ? authException.getMessage() : requestError.toString();

        if(log.isDebugEnabled()) {
            log.debug("rejecting access w/ {}, reason: {} ..", SC_UNAUTHORIZED, errorMsg);
        }

        response.sendError(SC_UNAUTHORIZED, errorMsg);
    }
}
