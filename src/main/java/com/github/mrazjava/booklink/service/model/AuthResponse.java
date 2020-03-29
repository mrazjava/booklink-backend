package com.github.mrazjava.booklink.service.model;

import java.time.OffsetDateTime;

/**
 * @author AZ (mrazjava)
 * @since 0.2.0
 */
public class AuthResponse {

    private String username;
    private String token;
    private OffsetDateTime tokenExpiry;
    private OffsetDateTime authenticatedOn;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public OffsetDateTime getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(OffsetDateTime tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }

    public OffsetDateTime getAuthenticatedOn() {
        return authenticatedOn;
    }

    public void setAuthenticatedOn(OffsetDateTime authenticatedOn) {
        this.authenticatedOn = authenticatedOn;
    }
}
