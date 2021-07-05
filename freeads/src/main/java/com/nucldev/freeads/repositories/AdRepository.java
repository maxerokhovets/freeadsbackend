package com.nucldev.freeads.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nucldev.freeads.entities.Ad;

public interface AdRepository extends JpaRepository<Ad, Long> {

}
