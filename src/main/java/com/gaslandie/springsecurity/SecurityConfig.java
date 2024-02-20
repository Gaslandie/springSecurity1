package com.gaslandie.springsecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.gaslandie.springsecurity.filtres.JwtAuthenticationFilter;
import com.gaslandie.springsecurity.filtres.JwtAuthorisationFilter;
import com.gaslandie.springsecurity.services.UserDetailsServiceImp;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private UserDetailsServiceImp userDetailsService;
    public SecurityConfig(UserDetailsServiceImp userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception{
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
    //configure le gestionnaire d'authentification
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService);
    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        
        // http.formLogin(Customizer.withDefaults()); //pour avoir le formulaire d'authentification
        return http.csrf(csrf -> csrf.disable())//car on veut utiliser seulement l'authentification stateless pour le moment
                                     .headers(headers -> headers.frameOptions().disable())
                                     .authorizeHttpRequests(request -> request
                                        .requestMatchers("/h2-console/**","/refreshToken/**","/login/**").permitAll()//pouvoir acceder à mon h2Console
                                        //.requestMatchers(HttpMethod.POST,"/users/**").hasAuthority("ADMIN")//seuls les admins peuvent ajouter un utilisateur
                                        //.requestMatchers(HttpMethod.GET,"/users/**").hasAuthority("USER")//un simple user peut voir les users
                                        .anyRequest().authenticated())
                                     .sessionManagement(dsl -> dsl.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//on declare officiellement qu'on veut utiliser l'authentification stateless
                                     .addFilter(new JwtAuthenticationFilter(authenticationManager(null)))
                                     .addFilterBefore(new JwtAuthorisationFilter(), UsernamePasswordAuthenticationFilter.class)
                                     .build();
    }
}
