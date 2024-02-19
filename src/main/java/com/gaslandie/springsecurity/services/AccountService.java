package com.gaslandie.springsecurity.services;

import java.util.List;

import com.gaslandie.springsecurity.entities.AppRole;
import com.gaslandie.springsecurity.entities.AppUser;

public interface AccountService {
    AppUser addNewUser(AppUser appUser);
    AppRole addNewRole(AppRole appRole);
    void addRoleToUser(String username,String rolename);
    AppUser loadUserByUsername(String username);
    List<AppUser> listUsers();
}
