package com.gaslandie.springsecurity.web;

import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaslandie.springsecurity.entities.AppRole;
import com.gaslandie.springsecurity.entities.AppUser;
import com.gaslandie.springsecurity.services.AccountService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PostAuthorize;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;






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
    //pour refresh notre token d'access,creer un nouvel access token
    @GetMapping("/refresh")
    public void refreshToken(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String authToken = request.getHeader("Authorization");
        if(authToken != null && authToken.startsWith("Bearer ")){

             try{
                String jwt = authToken.substring(7);//car notre refresh jwt commencera à l'index 7
                Algorithm algorithm = Algorithm.HMAC256("cleprivee");
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();//pour pouvoir verifier la validité de notre token
                DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
                String username = decodedJWT.getSubject();//car le subject est le username dans notre token
                AppUser appUser = accountService.loadUserByUsername(username);
                String jwtAccessToken = JWT.create()
                                .withSubject(appUser.getUsername())//username
                                .withExpiresAt(new Date(System.currentTimeMillis()+5*60*1000))//l'expiration après 5 mins
                                .withIssuer(request.getRequestURL().toString()) //le nom de l'application ayant generée le token
                                .withClaim("roles",appUser.getAppRoles().stream().map(r -> r.getRolename()).collect(Collectors.toList()))//extraction des roles de l'utilisateur
                                .sign(algorithm);//puis la signature
                Map<String,String> idToken = new HashMap<>();
                idToken.put("access-token",jwtAccessToken);
                idToken.put("refresh-token", jwt);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(),idToken);
            }catch (Exception e) {
                response.setHeader("error-message", e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
           }
        }else{
            throw new RuntimeException("refresh tokin required");
        }
    }
    //pour recuperer le profil  d'utilisateur authentifié
    @GetMapping("/profile")
    public AppUser profile(Principal principal) {
        return accountService.loadUserByUsername(principal.getName());
    }
    
    
    
}

//class qu'on va utiliser pour recuperer notre username et rolename dans l'ajout du role à un utilisateur
@Data //getter et setter avec lombok
class RoleUserForm{
    private String username;
    private String rolename;
}
