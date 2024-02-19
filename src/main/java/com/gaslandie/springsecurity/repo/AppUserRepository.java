package com.gaslandie.springsecurity.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gaslandie.springsecurity.entities.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    AppUser findByUsername(String username);
}
