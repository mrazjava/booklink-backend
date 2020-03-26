package com.github.mrazjava.booklink.security;

import com.github.mrazjava.booklink.persistence.model.UserEntity;
import com.github.mrazjava.booklink.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Authenticates users based on authorization token in headers. Success occurs
 * if a user produces a valid token (either existing or new) and the token is
 * NOT expired.
 *
 * @author AZ (mrazjava)
 * @since 0.2.0
 */
@Component
public class AccessTokenSecurityFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(AccessTokenSecurityFilter.class);

    /**
     * Auth token status is set every time if token is detected in the request.
     * It is validated even on requests which do not require auth token but
     * may have provided it in the header under {@link #AUTHORIZATION_HEADER_NAME}.
     * Requests which do not require authentication but provided a token are not
     * affected by this status.
     */
    public static final String ATTR_AUTH_TOKEN_STATUS = "AUTH-TOKEN";
    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    @Inject
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authToken = request.getHeader(AUTHORIZATION_HEADER_NAME);

        if(StringUtils.isNotEmpty(authToken)) {
            Optional<UserEntity> userResult = userService.fetchUserByAccessToken(authToken);
            if (userResult.isPresent()) {
                UserEntity user = userResult.get();
                log.trace("auth token [{}] matched; user:\n{}", user);
                if (OffsetDateTime.now().isBefore(user.getTokenExpiry())) {
                    log.debug("auth token [{}] is valid", authToken);
                    Authentication authentication = grantAuthority(user, authToken);
                    log.info("user [{}] granted access:\n{}", user.getEmail(), authentication);
                } else {
                    log.warn("auth token expired! (user: {})", user.getEmail());
                    request.setAttribute(ATTR_AUTH_TOKEN_STATUS, "expired token");
                }
            } else {
                log.trace("auth token [{}] is invalid", authToken);
                request.setAttribute(ATTR_AUTH_TOKEN_STATUS, "bad access token");
            }
        }

        filterChain.doFilter(request, response);
    }

    private Authentication grantAuthority(UserEntity user, String token) {

        Set<GrantedAuthority> roles = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(String.format("ROLE_%s", role.getName())))
                .collect(Collectors.toSet());

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, token, roles);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}
