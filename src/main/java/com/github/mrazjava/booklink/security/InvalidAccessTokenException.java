package com.github.mrazjava.booklink.security;

import com.github.mrazjava.booklink.BooklinkException;

/**
 * @author AZ
 */
public class InvalidAccessTokenException extends BooklinkException {

    public InvalidAccessTokenException(String message) {
        super(message);
    }
}
