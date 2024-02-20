package com.gaslandie.springsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.gaslandie.springsecurity.filtres.JwtAuthenticationFilter;
import com.gaslandie.springsecurity.filtres.JwtAuthorisationFilter;
import com.gaslandie.springsecurity.services.UserDetailsServiceImp;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //injection de userDetailsService par constructeur
    private UserDetailsServiceImp userDetailsService;
    public SecurityConfig(UserDetailsServiceImp userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    //pour obtenir l'objet authenticationManager utilisé pour authentifier l'utilisateur
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
    // @Autowired
    // AuthenticationManager authenticationManager;

    //configure le gestionnaire d'authentification
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService);//utiliser notre implementation personalisée de userDetailsService
    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        
        // http.formLogin(Customizer.withDefaults()); //pour avoir le formulaire d'authentification
        return http.csrf(csrf -> csrf.disable())//car on veut utiliser seulement l'authentification stateless pour le moment
                                     .headers(headers -> headers.frameOptions().disable())
                                     .authorizeHttpRequests(request -> request
                                        .requestMatchers("/h2-console/**","/refreshToken/**","/login/**","/**").permitAll()
                                        //.requestMatchers(HttpMethod.POST,"/users/**").hasAuthority("ADMIN")//seuls les admins peuvent ajouter un utilisateur
                                        //.requestMatchers(HttpMethod.GET,"/users/**").hasAuthority("USER")//un simple user peut voir les users
                                        .anyRequest().authenticated())
                                     .sessionManagement(dsl -> dsl.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//on declare officiellement qu'on veut utiliser l'authentification stateless
                                     .addFilter(new JwtAuthenticationFilter(authenticationManager(null)))//ajout du filtre d'authentification
                                     .addFilterBefore(new JwtAuthorisationFilter(), UsernamePasswordAuthenticationFilter.class)//ajouter avant,le filtre d'autorisation
                                     .build();
    }
}
