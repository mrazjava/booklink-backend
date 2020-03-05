package com.github.mrazjava.booklink;

import org.springframework.http.HttpStatus;

/**
 * @since 0.1.0
 */
public class BooklinkException extends RuntimeException {

    private final String requestParameters;

    private final HttpStatus httpErrorStatus;


    public BooklinkException(String message, Throwable cause, String requestParameters, HttpStatus httpErrorStatus) {
        super(message, cause);
        this.requestParameters = requestParameters;
        this.httpErrorStatus = httpErrorStatus;
    }

    public BooklinkException(String message) {
        this(message, null, null, null);
    }

    public BooklinkException(String message, Throwable cause) {
        this(message, cause, null, null);
    }

    public BooklinkException(String message, String requestParameters, HttpStatus httpErrorStatus) {
        this(message, null, requestParameters, httpErrorStatus);
    }

    public BooklinkException(String message, HttpStatus httpErrorStatus) {
        this(message, null, null, httpErrorStatus);
    }

    public BooklinkException(String message, Throwable cause, HttpStatus httpErrorStatus) {
        this(message, cause, null, httpErrorStatus);
    }

    public BooklinkException(String message, String requestParameters) {
        this(message, null, requestParameters, null);
    }

    public BooklinkException(String message, Throwable cause, String requestParameters) {
        this(message, cause, requestParameters, null);
    }

    public String getRequestParameters() {
        return requestParameters;
    }

    public HttpStatus getHttpErrorStatus() {
        return httpErrorStatus;
    }
}
