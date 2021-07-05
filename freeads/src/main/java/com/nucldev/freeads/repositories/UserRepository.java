package com.nucldev.freeads.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nucldev.freeads.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
