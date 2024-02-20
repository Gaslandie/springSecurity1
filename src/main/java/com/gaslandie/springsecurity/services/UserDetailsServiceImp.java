package com.gaslandie.springsecurity.services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gaslandie.springsecurity.entities.AppUser;

@Service
public class UserDetailsServiceImp implements UserDetailsService {
    AccountService accountService;
    public UserDetailsServiceImp(AccountService accountService) {
        this.accountService = accountService;
    }

    //la seule methode à implementer
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       AppUser appUser = accountService.loadUserByUsername(username);//recuperation du user depuis la base de données
                //crée une liste de roles pour le user
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                appUser.getAppRoles().forEach(r -> authorities.add(new SimpleGrantedAuthority(r.getRolename())));
                //crée un objet UserDetails avec les informations de l'utilisateur et ses authorisations
                return new User(appUser.getUsername(), appUser.getPassword(), authorities);
    }
    
}
