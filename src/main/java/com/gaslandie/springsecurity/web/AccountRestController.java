package com.gaslandie.springsecurity.web;

import org.springframework.web.bind.annotation.RestController;

import com.gaslandie.springsecurity.entities.AppUser;
import com.gaslandie.springsecurity.services.AccountService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class AccountRestController {
    private AccountService accountService;

    //pour l'injection des dependences au lieu d'utiliser autowired
    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }
    //recuper tous les utilisateurs
    @GetMapping("/users")
    public List<AppUser> appUsers() {
        return accountService.listUsers();
    }
    
    
}
