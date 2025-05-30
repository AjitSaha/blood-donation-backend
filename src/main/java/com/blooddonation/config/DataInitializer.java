package com.blooddonation.config;

import com.blooddonation.model.User;
import com.blooddonation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Create default user if no users exist
            if (userRepository.count() == 0) {
                User defaultUser = new User();
                defaultUser.setName("Default User");
                defaultUser.setEmail("user@example.com");
                defaultUser.setPassword(passwordEncoder.encode("password"));
                userRepository.save(defaultUser);
                
                System.out.println("Default user created: user@example.com / password");
            }
        };
    }
}