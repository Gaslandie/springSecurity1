package com.gaslandie.springsecurity.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaslandie.springsecurity.entities.AppRole;
import com.gaslandie.springsecurity.entities.AppUser;
import com.gaslandie.springsecurity.repo.AppRoleRepository;
import com.gaslandie.springsecurity.repo.AppUserRepository;

@Service
@Transactional //pour la gestion des transactions des methodes publics de cette classe
public class AccountServiceImpl implements AccountService {
    
    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    private PasswordEncoder passwordEncoder;
    

    //implementations des methodes de l'interface AccountService

    public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository,PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    //ajout nouvel utilisateur
    @Override
    public AppUser addNewUser(AppUser appUser) {
        //bcrypt pour hasher nos password avant de sauvergarder dans la bd
        String pw = appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(pw));
        return appUserRepository.save(appUser);
    }
    //ajout nouveau role
    @Override
    public AppRole addNewRole(AppRole appRole) {
         return appRoleRepository.save(appRole);
    }
    //ajout role a un utilisateur
    @Override
    public void addRoleToUser(String username, String rolename) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findByRolename(rolename);
        appUser.getAppRoles().add(appRole);
    }
    //recuperer un utilisateur
    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }
    //tous les utilisateurs
    @Override
    public List<AppUser> listUsers() {
       return appUserRepository.findAll();
    }
    
}
