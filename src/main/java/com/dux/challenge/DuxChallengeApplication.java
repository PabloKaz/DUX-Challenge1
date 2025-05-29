package com.dux.challenge;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.dux.challenge.service.UserService;

@SpringBootApplication
public class DuxChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DuxChallengeApplication.class, args);
	}
	
		/* Para que se ejecute el usuario Default al momento de arrancar el aplicativo */
	  @Bean
	  CommandLineRunner init(UserService userService) {
	    return args -> userService.initializeDefaultUser();
	  }
}
