package com.github.mrazjava.booklink;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.github.mrazjava.booklink.rest.model.ErrorResponse;
import com.github.mrazjava.booklink.security.InvalidAccessTokenException;

/**
 * Intercept all exceptions, those known to the app as well as unexpected one and
 * handle in a standardized way. Generally, errors known to the app return 409, while
 * unexpected ones return 500 (standard spring behavior).
 *
 * @author AZ (mrazjava)
 * @since 0.2.0
 */
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    static final String ERROR_MSG = "BOOKLINK ERROR";

    @Inject
    private Logger log;

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleUnexpectedEx(Exception ex, WebRequest request) {
        ErrorResponse response = produceErrorResponse(ex, request);
        log.error("unexpected problem:", ex);
        return new ResponseEntity<ErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse response = produceErrorResponse(ex, request);
        log.error("bad REST request", ex);
        return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BooklinkException.class)
    public final ResponseEntity<ErrorResponse> handleGenericBooklinkEx(Exception ex, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(produceErrorResponse(ex, request), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            AccessDeniedException.class,
            InvalidAccessTokenException.class
    })
    public final ResponseEntity<ErrorResponse> handleInvalidUserEx(Exception ex, WebRequest request) {
        log.info("security gate: {}", ex.getMessage());
        return new ResponseEntity<ErrorResponse>(produceErrorResponse(ex, request), HttpStatus.UNAUTHORIZED);
    }

    private ErrorResponse produceErrorResponse(Exception ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse(ERROR_MSG, ex.getMessage());
        response.setRequestData(request.getContextPath());
        if(ex.getCause() != null) {
            response.setExceptionRootCause(ex.getCause().getMessage());
        }
        return response;
    }
}
