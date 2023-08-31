package org.softtech.internship.backend.apigateway.config;

import org.softtech.internship.backend.apigateway.model.User;
import org.softtech.internship.backend.apigateway.util.JwtUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class GatewayConfig {

    private final WebClient.Builder webClientBuilder;
    private final JwtUtils jwtUtils;
    private final Map<String, User> userDatabase = new ConcurrentHashMap<>();

    public GatewayConfig(WebClient.Builder builder, JwtUtils utilTool) {
        this.webClientBuilder = builder;
        this.jwtUtils = utilTool;
        loadUsers();
    }

    private void loadUsers() {
        webClientBuilder.build()
                .get()
                .uri("http://localhost:9999/api/user/all-users")
                .retrieve()
                .bodyToMono(User[].class)
                .subscribe(users1 -> {
                    userDatabase.clear();
                    for (User user : users1) {
                        userDatabase.put(user.getUsername(), user);
                    }
                });
    }

    public void refreshUsers() {
        loadUsers();
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/api/user/**")
                        .uri("lb://user-service"))
                .route("inventory-service", r -> r.path("/api/inventory/**")
                        .filters(f -> f.filter(jwtValidationFilter()))
                        .uri("lb://inventory-service"))
                .build();
    }

    @Bean
    public GatewayFilter jwtValidationFilter() {
        return (exchange, chain) -> {
            String token = jwtUtils.extractTokenFromRequest(exchange);
            if (token != null) {
                String username = jwtUtils.extractUsername(token);
                if (username != null && userDatabase.containsKey(username)) {
                    User user = userDatabase.get(username);
                    if (user != null && jwtUtils.validateToken(token, user)) {
                        return chain.filter(exchange);
                    }
                }
            }
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        };
    }
}
