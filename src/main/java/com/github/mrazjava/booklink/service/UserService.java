package com.github.mrazjava.booklink.service;

import com.github.mrazjava.booklink.BooklinkException;
import com.github.mrazjava.booklink.persistence.model.UserEntity;
import com.github.mrazjava.booklink.persistence.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author AZ(mrazjava)
 * @since 0.2.0
 */
@Service
public class UserService {

    @Inject
    private Logger log;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    private final Pattern uuidPattern = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");


    public UserEntity authenticate(String email, String password) {

        Optional<UserEntity> userResult = findUserByEmail(email);
        String error = null;

        if(userResult.isPresent()) {
            UserEntity user = userResult.get();
            log.debug("user exists:\n{}", user);
            log.trace("provided pwd: {}, stored pwd: {}", password, user.getPassword());
            if(passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
            else {
                error = String.format("pwd mismatch: [%s]", password);
            }
        }
        else {
            error = String.format("user not found; email=%s", email);
        }

        if(error == null) {
            error = "authentication failed";
        }

        throw new BadCredentialsException(error);

    }

    public Optional<UserEntity> findUserByEmail(String email) {

        return userRepository.findByEmail(email);
    }

    public Optional<UserEntity> fetchUserByAccessToken(String accessToken) {

        if(StringUtils.isEmpty(accessToken) || !uuidPattern.matcher(accessToken).matches()) {
            return Optional.empty();
        }

        return userRepository.findByToken(accessToken);
    }

    /**
     * If access token is not present or if it's expired, new one is created. If token
     * exists and is not expired, it is returned.
     *
     * @param userEntity for which to verify the token
     * @return user with a valid token
     * @throws BooklinkException if loginId is invalid
     */
    public UserEntity ensureValidToken(UserEntity userEntity) {

        UserEntity validatedUser = userEntity;

        if(StringUtils.isEmpty(userEntity.getToken()) || OffsetDateTime.now().isAfter(userEntity.getTokenExpiry())) {
            userEntity.setToken(UUID.randomUUID().toString());
            userEntity.setTokenExpiry(OffsetDateTime.now().plusDays(3));
            log.debug("issued new access token {}, expiry: {}", userEntity.getToken(), userEntity.getTokenExpiry());
            validatedUser = userRepository.save(userEntity);
        }

        return validatedUser;
    }
}
