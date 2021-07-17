package com.nucldev.freeads.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nucldev.freeads.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    
    Optional<User> findByUsernameOrEmail(String username, String email);
    
    Boolean existsByUsername(String username);
    
    Boolean existsByEmail(String email);
}
