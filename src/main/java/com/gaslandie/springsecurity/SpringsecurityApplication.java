package com.gaslandie.springsecurity;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gaslandie.springsecurity.entities.AppRole;
import com.gaslandie.springsecurity.entities.AppUser;
import com.gaslandie.springsecurity.services.AccountService;

@SpringBootApplication
// @EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true), deprecié
@EnableMethodSecurity(prePostEnabled = true,securedEnabled = true)//une autre façon pour gerer l'autorisation
public class SpringsecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringsecurityApplication.class, args);
	}
	//@Bean pour mettre dans spring context,utilisation bcrypt pour nos mots de passe
	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner start(AccountService accountService){//qui va s'executer au demarrage
		return args -> {
			//creation de roles
			accountService.addNewRole(new AppRole(null,"USER"));
			accountService.addNewRole(new AppRole(null,"ADMIN"));
			accountService.addNewRole(new AppRole(null,"CUSTOMER_MANAGER"));
			accountService.addNewRole(new AppRole(null,"PRODUCT_MANAGER"));
			accountService.addNewRole(new AppRole(null,"BILLS_MANAGER"));

			//creation de users
			accountService.addNewUser(new AppUser(null,"user1","abcd",new ArrayList<>()));
			accountService.addNewUser(new AppUser(null,"admin","abcd",new ArrayList<>()));
			accountService.addNewUser(new AppUser(null,"user2","abcd",new ArrayList<>()));
			accountService.addNewUser(new AppUser(null,"user3","abcd",new ArrayList<>()));
			accountService.addNewUser(new AppUser(null,"user4","abcd",new ArrayList<>()));

			//roles de nos users
			//user1
			accountService.addRoleToUser("user1", "USER");
			//admin
			accountService.addRoleToUser("admin", "USER");
			accountService.addRoleToUser("admin", "ADMIN");
			
			//user2
			accountService.addRoleToUser("user2", "USER");
			accountService.addRoleToUser("user2", "CUSTOMER_MANAGER");
			//user3
			accountService.addRoleToUser("user3", "USER");
			accountService.addRoleToUser("user3", "PRODUCT_MANAGER");
			//user4
			accountService.addRoleToUser("user4", "USER");
			accountService.addRoleToUser("user4", "BILLS_MANAGER");
			
		};
		
	}
}
