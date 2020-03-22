package com.github.mrazjava.booklink.persistence.repository;

import com.github.mrazjava.booklink.persistence.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author AZ (mrazjava)
 * @since 0.2.0
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByToken(String token);
}
