package com.github.mrazjava.booklink.service;

import com.github.mrazjava.booklink.TestLogger;
import com.github.mrazjava.booklink.persistence.model.UserEntity;
import com.github.mrazjava.booklink.persistence.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.inject.Inject;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author AZ (mrazjava)
 * @since 0.2.0
 */
@SpringJUnitConfig
@Import({
        UserService.class,
        TestLogger.class
})
public class UserServiceTest {

    @Inject
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void shouldRejectValidateTokenBadCredentials() {
        Assertions.assertThrows(
                BadCredentialsException.class,
                () -> userService.login(new Object())
        );
    }

    @Test
    public void shouldAcceptValidToken() {
        String token = UUID.randomUUID().toString();
        OffsetDateTime tokenExpiry = OffsetDateTime.now().plusDays(1);
        UserEntity user = new UserEntity(token, tokenExpiry);
        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        when(userRepository.save(any())).thenReturn(user);
        UserEntity verifiedUser = userService.login(user);
        verify(userRepository, times(1)).save(captor.capture());
        UserEntity loggedInUser = captor.getValue();
        assertNotNull(loggedInUser.getLastLoginOn());
        assertEquals(token, verifiedUser.getToken());
    }

    @Test
    public void shouldIssueNewToken() {

        String token = UUID.randomUUID().toString(), newToken = UUID.randomUUID().toString();
        OffsetDateTime tokenExpiry = OffsetDateTime.now().minusDays(1);

        when(userRepository.save(any(UserEntity.class))).thenReturn(new UserEntity(newToken, null));

        UserEntity verifiedUser = userService.login(new UserEntity(token, tokenExpiry));

        assertNotNull(verifiedUser.getToken());
        assertEquals(newToken, verifiedUser.getToken());
    }
}
