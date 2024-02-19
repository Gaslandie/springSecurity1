package com.gaslandie.springsecurity.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gaslandie.springsecurity.entities.AppRole;

public interface AppRoleRepository extends JpaRepository<AppRole,Long> {
    AppRole findByRoleName(String rolename);
}
