package org.softtech.internship.backend.login.config;

import lombok.RequiredArgsConstructor;
import org.softtech.internship.backend.login.model.user.Role;
import org.softtech.internship.backend.login.model.user.User;
import org.softtech.internship.backend.login.repository.UserRepository;
import org.softtech.internship.backend.login.util.HashHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    @Bean
    public CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return args -> {
            userRepository.save(User.builder().username("admin").password(HashHandler.getHashedPassword("admin")).role(Role.ADMIN).build());
            userRepository.save(User.builder().username("user").password(HashHandler.getHashedPassword("password")).role(Role.USER).build());
            userRepository.flush();
        };
    }

}

