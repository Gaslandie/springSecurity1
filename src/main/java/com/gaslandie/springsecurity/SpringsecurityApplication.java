package com.gaslandie.springsecurity;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.gaslandie.springsecurity.entities.AppRole;
import com.gaslandie.springsecurity.entities.AppUser;
import com.gaslandie.springsecurity.services.AccountService;

@SpringBootApplication
public class SpringsecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringsecurityApplication.class, args);
	}
	@Bean //pour la gestion et injection par spring conteneur du resultat de cette methode
	CommandLineRunner start(AccountService accountService){ //pour executer au demarrage de l'application
		return args -> {

			//ajout de role dans la db
			accountService.addNewRole(new AppRole(null,"USER"));
			accountService.addNewRole(new AppRole(null,"ADMIN"));
			accountService.addNewRole(new AppRole(null,"CUSTOMER_MANAGER"));
			accountService.addNewRole(new AppRole(null,"PRODUCT_MANAGER"));
			accountService.addNewRole(new AppRole(null,"BILLS_MANAGER"));

			//ajout de users dans notre db
			accountService.addNewUser(new AppUser(null,"user1","abcd",new ArrayList<>()));
			accountService.addNewUser(new AppUser(null,"admin","abcd",new ArrayList<>()));
			accountService.addNewUser(new AppUser(null,"user2","abcd",new ArrayList<>()));
			accountService.addNewUser(new AppUser(null,"user3","abcd",new ArrayList<>()));
			accountService.addNewUser(new AppUser(null,"user4","abcd",new ArrayList<>()));

			//ajout de role à nos users
			accountService.addRoleToUser("user1", "USER");
			//admin a les roles user et admin
			accountService.addRoleToUser("admin", "USER");
			accountService.addRoleToUser("admin", "ADMIN");
			//user2 est user et custom manager
			accountService.addRoleToUser("user2", "USER");
			accountService.addRoleToUser("user2", "CUSTOM_MANAGER");
			//user3 est user et product manager
			accountService.addRoleToUser("user3", "USER");
			accountService.addRoleToUser("user3", "PRODUCT_MANAGER");
			//user4 est user et Bills manager
			accountService.addRoleToUser("user4", "USER");
			accountService.addRoleToUser("user4", "BILLS_MANAGER");
		};
	}
}
