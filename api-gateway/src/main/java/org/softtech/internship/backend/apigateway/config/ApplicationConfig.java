package org.softtech.internship.backend.apigateway.config;

import org.softtech.internship.backend.apigateway.util.JwtUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {
    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }
}
