package com.github.mrazjava.booklink.persistence.repository;

import com.github.mrazjava.booklink.persistence.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author AZ (mrazjava)
 * @since 0.2.0
 */
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}
