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

    @Override
    public AppUser addNewUser(AppUser appUser) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addNewUser'");
    }

    @Override
    public AppUser addNewRole(AppRole appRole) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addNewRole'");
    }

    @Override
    public void addRoleToUser(String username, String rolename) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addRoleToUser'");
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
    }

    @Override
    public List<AppUser> listUsers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listUsers'");
    }
    
}
