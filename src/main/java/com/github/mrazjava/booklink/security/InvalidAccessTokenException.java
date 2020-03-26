package com.github.mrazjava.booklink.security;

import com.github.mrazjava.booklink.BooklinkException;

/**
 * @author AZ (mrazjava)
 * @since 0.2.0
 */
public class InvalidAccessTokenException extends BooklinkException {

    public InvalidAccessTokenException(String message) {
        super(message);
    }
}
