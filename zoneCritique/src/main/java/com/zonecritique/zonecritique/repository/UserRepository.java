package com.zonecritique.zonecritique.repository;

import com.zonecritique.zonecritique.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(int username);
    User findByUsername(String username);

    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
}
