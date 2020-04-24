package com.github.mrazjava.booklink.service;

import com.github.mrazjava.booklink.BooklinkException;
import com.github.mrazjava.booklink.persistence.model.RoleEntity;
import com.github.mrazjava.booklink.persistence.model.UserEntity;
import com.github.mrazjava.booklink.persistence.repository.RoleRepository;
import com.github.mrazjava.booklink.persistence.repository.UserRepository;
import com.github.mrazjava.booklink.rest.model.LoginRequest;
import com.github.mrazjava.booklink.security.InvalidAccessTokenException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * @author AZ(mrazjava)
 * @since 0.2.0
 */
@Service
public class UserService implements UserDetailsService {

    @Inject
    private Logger log;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    private final Pattern uuidPattern = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");


    public Optional<UserEntity> findUserByEmail(String email) {

        return userRepository.findByEmail(email);
    }

    public Optional<UserEntity> fetchUserByAccessToken(String accessToken) {

        if(StringUtils.isEmpty(accessToken) || !uuidPattern.matcher(accessToken).matches()) {
            return Optional.empty();
        }

        return userRepository.findByToken(accessToken);
    }

    public void prepareFacebookLogin(LoginRequest request) {

        Optional<UserEntity> result = findUserByEmail(request.getEmail());
        UserEntity userEntity;

        if(result.isEmpty()) {
            // auto register FB user
            log.debug("new facebook user request:\n{}", request);

            userEntity = new UserEntity(UUID.randomUUID().toString(), OffsetDateTime.now().plusDays(10));
            userEntity.setFirstName(request.getFbFirstName());
            userEntity.setLastName(request.getFbLastName());
            userEntity.setEmail(request.getEmail());
            userEntity.setPassword(passwordEncoder.encode(request.getFbId()));
            userEntity.setActive(1);
            userEntity.setRoles(Set.of(new RoleEntity(RoleEntity.ID_DETECTIVE)));
            userEntity = userRepository.save(userEntity);

        }
        else {
            userEntity = result.get();
        }

        request.setPassword(request.getFbId());
    }

    /**
     * If access token is not present or if it's expired, new one is created. If token
     * exists and is not expired, it is returned.
     *
     * @param principal for which to verify the token as provided by authentication manager
     * @return user with a valid token
     * @throws BadCredentialsException if principal is of wrong type
     */
    public UserEntity login(Object principal) {

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

        validatedUser.setLastLoginOn(OffsetDateTime.now());

        return userRepository.save(validatedUser);
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

        Optional<UserEntity> userEntityResult = findUserByEmail(username);
        userEntityResult.orElseThrow(() -> new UsernameNotFoundException("invalid user"));
        return userEntityResult.map(Function.identity()).get();
    }

    public static UserDetails getCredentials(Authentication auth) {

        if(auth == null || auth.getPrincipal() == null) {
            throw new InvalidAccessTokenException("no user credentials");
        }

        return (UserDetails)auth.getPrincipal();
    }
}
