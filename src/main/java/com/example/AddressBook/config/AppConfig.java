package com.example.AddressBook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AppConfig {

    @Bean
    @Profile("dev")
    public String devBean() {
        return "Development Bean Loaded";
    }

    @Bean
    @Profile("prod")
    public String prodBean() {
        return "Production Bean Loaded";
    }

    @Bean
    @Profile("staging")
    public String stagingBean() {
        return "Staging Bean Loaded";
    }
}
