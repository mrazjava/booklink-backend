package com.github.mrazjava.booklink.security;

import com.github.mrazjava.booklink.BooklinkException;

/**
 * @author AZ
 */
public class InvalidAccessTokenException extends BooklinkException {

	private static final long serialVersionUID = -47687245348086308L;

	public InvalidAccessTokenException(String message) {
        super(message);
    }

    public InvalidAccessTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
