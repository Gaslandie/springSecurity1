package com.gaslandie.springsecurity;

import org.springframework.stereotype.Component;

import lombok.Data;

//class qu'on va utiliser pour recuperer notre username et rolename dans l'ajout du role à un utilisateur
@Component
@Data //getter et setter avec lombok
public class RoleUserForm{
    private String username;
    private String rolename;
}
