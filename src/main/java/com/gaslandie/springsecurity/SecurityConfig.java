package com.gaslandie.springsecurity;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

import com.gaslandie.springsecurity.entities.AppUser;
import com.gaslandie.springsecurity.services.AccountService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private AccountService accountService;

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception{
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
    //configure le gestionnaire d'authentification
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(new UserDetailsService() {
            //recupere les details d'un user à l'aide du username
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                AppUser appUser = accountService.loadUserByUsername(username);//recuperation du user depuis la base de données
                //crée une liste de roles pour le user
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                appUser.getAppRoles().forEach(r -> authorities.add(new SimpleGrantedAuthority(r.getRolename())));
                //crée un objet UserDetails avec les informations de l'utilisateur et ses authorisations
                return new User(appUser.getUsername(), appUser.getPassword(), authorities);
            }
        });
    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        
        // http.formLogin(Customizer.withDefaults()); //pour avoir le formulaire d'authentification
        return http.csrf(csrf -> csrf.disable())//car on veut utiliser l'authentification stateless
                                     .headers(headers -> headers.frameOptions().disable())
                                     .authorizeHttpRequests(request -> request
                                        .requestMatchers("/h2-console/**").permitAll()//pouvoir acceder à mon h2Console
                                        .anyRequest().authenticated())
                                     .sessionManagement(dsl -> dsl.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//on declare officiellement qu'on veut utiliser l'authentification stateless
                                     .addFilter(new JwtAuthenticationFilter(authenticationManager(null)))
                                     .build();
    }
}
