package com.gaslandie.springsecurity.entities;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor //à l'aide la programmation orientée aspect: lombok nous permet de reduire notre code
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)//ne pas avoir le mot passe dans le json
    private String password;
    
    @ManyToMany(fetch = FetchType.EAGER) //pour recuperer les users avec les roles
    private Collection<AppRole> appRoles = new ArrayList<>();
}
