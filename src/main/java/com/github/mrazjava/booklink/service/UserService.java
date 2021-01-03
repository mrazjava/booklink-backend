package com.github.mrazjava.booklink.service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.mrazjava.booklink.persistence.model.RoleEntity;
import com.github.mrazjava.booklink.persistence.model.UserEntity;
import com.github.mrazjava.booklink.persistence.model.UserOrigin;
import com.github.mrazjava.booklink.persistence.model.UserOriginEntity;
import com.github.mrazjava.booklink.persistence.repository.UserRepository;
import com.github.mrazjava.booklink.rest.model.LoginRequest;
import com.github.mrazjava.booklink.security.InvalidAccessTokenException;

import static java.util.Optional.ofNullable;

/**
 * @author AZ
 */
@Service
public class UserService implements UserDetailsService {

    @Inject
    private Logger log;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    private Pattern uuidPattern;

    @Value("${booklink.auth-token-pattern:#{null}}")
    private String authTokenPattern;

    @PostConstruct
    void init() {
    	if(StringUtils.isNotBlank(authTokenPattern)) {
    		log.info("initializing autho token paten to: {}", authTokenPattern);
    		uuidPattern = Pattern.compile(authTokenPattern);
    	}
    }
    
    public Optional<UserEntity> findUserByEmail(String email) {

        return userRepository.findByEmail(email);
    }

    public Optional<UserEntity> fetchUserByAccessToken(String accessToken) {

        if(StringUtils.isEmpty(accessToken) || ofNullable(uuidPattern).map(p -> p.matcher(accessToken).matches()).orElse(false)) {
            return Optional.empty();
        }

        return userRepository.findByToken(accessToken);
    }

    public void prepareFacebookLogin(LoginRequest request) {

        Optional<UserEntity> result = findUserByEmail(request.getEmail());
        UserEntity userEntity;

        if(result.isEmpty()) {
            // auto register FB user
            log.debug("new FACEBOOK user request:\n{}", request);

            userEntity = new UserEntity(UUID.randomUUID().toString(), OffsetDateTime.now().plusDays(10));
            userEntity.setFirstName(request.getSmFirstName());
            userEntity.setLastName(request.getSmLastName());
            userEntity.setEmail(request.getEmail());
            userEntity.setPasswordFb(passwordEncoder.encode(request.getSmId()));
            userEntity.setActive(1);
            userEntity.setRoles(Set.of(new RoleEntity(RoleEntity.ID_DETECTIVE)));
            userEntity.setRegistrationOrigin(new UserOriginEntity(UserOrigin.FACEBOOK.getId()));
            userEntity = userRepository.save(userEntity);

        }
        else {
            userEntity = result.get();
            if(StringUtils.isEmpty(userEntity.getPasswordFb())) {
                // user already setup under different origin but first time FB login
                userEntity.setPasswordFb(passwordEncoder.encode(request.getSmId()));
                userRepository.save(userEntity);
            }
        }

        // fb auth does not provide password in the request; we need to forward one for the purposes of auth
        request.setPassword(request.getSmId());
    }

    public void prepareGoogleLogin(LoginRequest request) {

        Optional<UserEntity> result = findUserByEmail(request.getEmail());
        UserEntity userEntity;

        if(result.isEmpty()) {
            // auto register GL user
            log.debug("new GOOGLE user request:\n{}", request);

            userEntity = new UserEntity(UUID.randomUUID().toString(), OffsetDateTime.now().plusDays(10));
            userEntity.setFirstName(request.getSmFirstName());
            userEntity.setLastName(request.getSmLastName());
            userEntity.setEmail(request.getEmail());
            userEntity.setPasswordGl(passwordEncoder.encode(request.getSmId()));
            userEntity.setActive(1);
            userEntity.setRoles(Set.of(new RoleEntity(RoleEntity.ID_DETECTIVE)));
            userEntity.setRegistrationOrigin(new UserOriginEntity(UserOrigin.GOOGLE.getId()));
            userEntity = userRepository.save(userEntity);

        }
        else {
            userEntity = result.get();
            if(StringUtils.isEmpty(userEntity.getPasswordFb())) {
                // user already setup under different origin but first time GL login
                userEntity.setPasswordGl(passwordEncoder.encode(request.getSmId()));
                userRepository.save(userEntity);
            }
        }

        // gl auth does not provide password in the request; we need to build one
        request.setPassword(request.getSmId());
    }

    /**
     * If access token is not present or if it's expired, new one is created. If token
     * exists and is not expired, it is returned.
     *
     * @param principal for which to verify the token as provided by authentication manager
     * @param loginOrigin source from which user authenticated
     * @return user with a valid token
     * @throws BadCredentialsException if principal is of wrong type
     */
    public UserEntity login(Object principal, UserOrigin loginOrigin) {

        if(!(principal instanceof UserEntity)) {
            throw new BadCredentialsException("wrong principal");
        }

        UserEntity validatedUser = (UserEntity)principal;

        if(validatedUser.isTokenExpired()) {
            validatedUser.setToken(UUID.randomUUID().toString());
            validatedUser.setTokenExpiry(OffsetDateTime.now().plusDays(3));
            if(log.isDebugEnabled()) {
                log.debug("issued new access token {}, expiry: {}", validatedUser.getToken(), validatedUser.getTokenExpiry());
            }
        }

        OffsetDateTime lastLoginOn = validatedUser.getLastLoginOn();
        validatedUser.setLastLoginOn(OffsetDateTime.now());
        validatedUser.setLastLoginOrigin(new UserOriginEntity(loginOrigin.getId()));
        UserEntity loginResult = userRepository.save(validatedUser);
        loginResult.setLastLoginOn(lastLoginOn);

        return loginResult;
    }

    public String deleteAuthToken(UserDetails credentials) {
        Optional<UserEntity> userEntityResult = findUserByEmail(credentials.getUsername());
        if(!userEntityResult.isPresent()) {
            throw new UsernameNotFoundException("invalid user");
        }
        UserEntity userEntity = userEntityResult.get();
        userEntity.setToken(null);
        userEntity.setTokenExpiry(null);
        userEntity = userRepository.save(userEntity);
        return userEntity.getEmail();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("spring auth; fetching user: {}", username);

        if(!username.contains("#")) {
            log.error("malformed username: {} (expected format: ORIGIN#EMAIL)", username);
            throw new UsernameNotFoundException(username);
        }

        String[] userNameTokens = username.split("#");

        if(userNameTokens.length != 2) {
            log.error("bad username token setup: {} (expecting exactly 2 tokens)");
            throw new UsernameNotFoundException(username);
        }

        UserOrigin loginOrigin = UserOrigin.valueOf(userNameTokens[0]);
        String email = userNameTokens[1];

        Optional<UserEntity> userEntityResult = findUserByEmail(email);
        userEntityResult.orElseThrow(() -> new UsernameNotFoundException("invalid user"));
        userEntityResult.get().setLastLoginOrigin(new UserOriginEntity(loginOrigin.getId()));
        return userEntityResult.map(Function.identity()).get();
    }

    public static UserDetails getCredentials(Authentication auth) {

        if(auth == null || auth.getPrincipal() == null) {
            throw new InvalidAccessTokenException("no user credentials");
        }

        return (UserDetails)auth.getPrincipal();
    }
}
