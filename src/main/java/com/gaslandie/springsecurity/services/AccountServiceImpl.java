package com.gaslandie.springsecurity.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaslandie.springsecurity.entities.AppRole;
import com.gaslandie.springsecurity.entities.AppUser;
import com.gaslandie.springsecurity.repo.AppRoleRepository;
import com.gaslandie.springsecurity.repo.AppUserRepository;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    
    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    

    public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
    }

    @Override
    public AppUser addNewUser(AppUser appUser) {
       return appUserRepository.save(appUser);
    }

    @Override
    public AppRole addNewRole(AppRole appRole) {
        return appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String rolename) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findByRoleName(rolename);
        appUser.getAppRoles().add(appRole);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> listUsers() {
       return appUserRepository.findAll();
    }
    
}
