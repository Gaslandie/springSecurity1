package com.gaslandie.springsecurity.web;

import org.springframework.web.bind.annotation.RestController;

import com.gaslandie.springsecurity.entities.AppRole;
import com.gaslandie.springsecurity.entities.AppUser;
import com.gaslandie.springsecurity.services.AccountService;

import lombok.Data;

import java.util.List;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class AccountRestController {
    private AccountService accountService;

    //constructeur pour l'injection des dependences au lieu d'utiliser autowired
    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }
    //recuper tous les utilisateurs
    @GetMapping("/users")
    @PostAuthorize("hasAutority('USER')")
    public List<AppUser> appUsers() {
        return accountService.listUsers();
    }
    //ajouter un utilisateur
    @PostMapping("/users")
    @PostAuthorize("hasAutority('ADMIN')")
    public AppUser saveUser(@RequestBody AppUser appUser) { 
        return accountService.addNewUser((appUser));
    }
    
    //ajouter un role
    @PostMapping("/roles")
    public AppRole saveRole(@RequestBody AppRole appRole) {
        return accountService.addNewRole(appRole);
    }
    //ajouter un role à un utilisateur
    @PostMapping("/addRoleToUser")
    public void addRoleToUser(@RequestBody RoleUserForm roleUserForm) {
        accountService.addRoleToUser(roleUserForm.getUsername(), roleUserForm.getRolename());
    }
    
}

//class qu'on va utiliser pour recuperer notre username et rolename dans l'ajout du role à un utilisateur
@Data //getter et setter avec lombok
class RoleUserForm{
    private String username;
    private String rolename;
}
