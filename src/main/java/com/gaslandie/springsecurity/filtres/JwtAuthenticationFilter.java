package com.gaslandie.springsecurity.filtres;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    private AuthenticationManager authenticationManager;
    //injection par constructor plutot que par autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }
    //quand l'utilisateur va essayer de s'authentifier
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //on recupere le mot de passe et le nom d'utilisateur
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username);
        System.out.println(password);
        //on va les stocker dans un objet de type UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        //retourner à spring manager cet objet
        return authenticationManager.authenticate(authenticationToken);
        }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,Authentication authResult) throws IOException, ServletException {
        User user = (User)authResult.getPrincipal();//pour retourner l'utilisateur authentifié,on caste car getprincipal retourn un objet de type Object
        //generer le token après avoir chercher la bibliotheque auth0 jwt token depuis maeven
        Algorithm algorithm = Algorithm.HMAC256("cleprivee");
        //les infos du payload de notre accessToken
        String jwtAccessToken = JWT.create()
                                .withSubject(user.getUsername())//username
                                .withExpiresAt(new Date(System.currentTimeMillis()+5*60*1000))//l'expiration après 5 mins
                                .withIssuer(request.getRequestURL().toString()) //le nom de l'application ayant generée le token
                                .withClaim("roles",user.getAuthorities().stream().map(ga -> ga.getAuthority()).collect(Collectors.toList()))//extraction des roles de l'utilisateur
                                .sign(algorithm);//puis la signature
        //les infos du payload de notre refresh token
        String jwtRefreshToken = JWT.create()
                                .withSubject(user.getUsername())//username
                                .withExpiresAt(new Date(System.currentTimeMillis()+20*60*1000))//l'expiration après 20 mins
                                .withIssuer(request.getRequestURL().toString()) //le nom de l'application ayant generée le token
                                .sign(algorithm);//puis la signature
        //envoyer nos token dans une map
        Map<String,String> idToken = new HashMap<>();
        idToken.put("access-token",jwtAccessToken);
        idToken.put("refresh-token", jwtRefreshToken);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(),idToken);
        //response.setHeader("Authorization", jwtAccessToken1);//mettre le token dans len tete de notre reponse envoyé au client
    }
    
}
