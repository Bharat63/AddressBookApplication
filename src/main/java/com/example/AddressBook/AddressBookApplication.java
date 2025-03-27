package com.example.AddressBook;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;

@EnableCaching
@SpringBootApplication
public class AddressBookApplication {

	@Value("${spring.profiles.active}")
	private String activeProfile;

	public static void main(String[] args) {
		SpringApplication.run(AddressBookApplication.class, args);
	}

	@Bean
	CommandLineRunner printActiveProfile() {
		return args -> System.out.println("🔥 Active Profile: " + activeProfile);
	}
}
