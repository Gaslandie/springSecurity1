package com.gaslandie.springsecurity.filtres;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthorisationFilter  extends OncePerRequestFilter{
    //a chaque fois qu'il ya une requete qui arrive
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException{
        //recuperation de notre token d'autorisation s'il yen a
        String authorizationToken = request.getHeader("Authorization");
        if(authorizationToken != null && authorizationToken.startsWith("Bearer ")){

            try{
                String jwt = authorizationToken.substring(7);//car notre jwt commencera à l'index 7
                Algorithm algorithm = Algorithm.HMAC256("cleprivee");
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();//pour pouvoir verifier la validité de notre token
                DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
                String username = decodedJWT.getSubject();//car le subject est le username dans notre token
                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                for(String r:roles){
                    authorities.add(new SimpleGrantedAuthority(r));
                }
                UsernamePasswordAuthenticationToken authenticationToken = 
                                new UsernamePasswordAuthenticationToken(username,null,authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);//ajout dans spring context
                filterChain.doFilter(request, response);//passer au filtre suivant comme les middlewares dans nodejs
            }catch (Exception e) {
                response.setHeader("error-message", e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
           }
            
        }else{
            filterChain.doFilter(request, response);
        }
    }
    
}
