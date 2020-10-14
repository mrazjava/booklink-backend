package com.github.mrazjava.booklink.integration;

import com.github.mrazjava.booklink.persistence.model.UserEntity;
import com.github.mrazjava.booklink.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * @author AZ (mrazjava)
 * @since 0.2.0
 */
@ExtendWith(SpringExtension.class)
@Sql("/it/user-repository.sql")
@DataJpaTest
public class UserRepositoryTest {

    @Inject
    private UserRepository userRepository;

    @Test
    void shouldFindUserByEmail() {
        Optional<UserEntity> result = userRepository.findByEmail("zorror@pueblo.com");
        assertTrue(result.isPresent());
    }

    @Sql("/it/user-repository-count.sql")
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Test
    void shouldCount() {
        assertEquals(2, userRepository.count());
    }


}
