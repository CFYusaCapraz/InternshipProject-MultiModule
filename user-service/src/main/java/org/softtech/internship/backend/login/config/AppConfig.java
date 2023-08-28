package org.softtech.internship.backend.login.config;

import lombok.RequiredArgsConstructor;
import org.softtech.internship.backend.login.model.user.Role;
import org.softtech.internship.backend.login.model.user.User;
import org.softtech.internship.backend.login.repository.UserRepository;
import org.softtech.internship.backend.login.util.HashHandler;
import org.softtech.internship.backend.login.util.JwtHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final UserRepository userRepository;

    @Bean
    public JwtHandler jwtHandler() {
        return new JwtHandler();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username ->
                userRepository.findUserByUsernameAndIsDeletedIsFalse(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return HashHandler.getHashedPassword(rawPassword.toString());
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return HashHandler.getHashedPassword(rawPassword.toString()).equals(encodedPassword);
            }
        };
    }

    @Bean
    public CommandLineRunner  commandLineRunner(UserRepository  userRepository) {
        return args -> {
            userRepository.save(User.builder().username("admin").password(HashHandler.getHashedPassword("admin")).role(Role.ADMIN).build());
            userRepository.save(User.builder().username("user").password(HashHandler.getHashedPassword("password")).role(Role.USER).build());
            userRepository.flush();
        };
    }

}

