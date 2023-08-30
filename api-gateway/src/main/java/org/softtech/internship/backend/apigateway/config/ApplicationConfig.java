package org.softtech.internship.backend.apigateway.config;

import org.softtech.internship.backend.apigateway.util.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }
}
