package com.github.mrazjava.booklink.persistence.repository;

import com.github.mrazjava.booklink.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
