package com.spring_security_30.repository;

import com.spring_security_30.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username ,String email);
    Optional<User>findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
