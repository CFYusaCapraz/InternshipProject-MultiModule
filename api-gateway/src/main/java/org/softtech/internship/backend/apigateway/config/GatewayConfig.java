package org.softtech.internship.backend.apigateway.config;

import org.softtech.internship.backend.apigateway.model.User;
import org.softtech.internship.backend.apigateway.service.GatewayService;
import org.softtech.internship.backend.apigateway.util.JwtUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@Configuration
public class GatewayConfig {
    private final JwtUtils jwtUtils;
    private final GatewayService gatewayService;

    public GatewayConfig(JwtUtils jwtUtils, GatewayService gatewayService) {
        this.jwtUtils = jwtUtils;
        this.gatewayService = gatewayService;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/api/user/**")
                        .uri("lb://user-service"))
                .route("inventory-service", r -> r.path("/api/inventory/**")
                        .filters(f -> f.filter(jwtValidationFilter())
                                .filter(currencyPermissionFilter()))
                        .uri("lb://inventory-service"))
                .build();
    }

    @Bean
    public GatewayFilter jwtValidationFilter() {
        return (exchange, chain) -> {
            String token = jwtUtils.extractTokenFromRequest(exchange);
            if (token != null) {
                if (gatewayService.isTokenRegistered(token)) {
                    String username = jwtUtils.extractUsername(token);
                    if (username != null && gatewayService.getUserDatabase().containsKey(username)) {
                        User user = gatewayService.getUserDatabase().get(username);
                        if (user != null && jwtUtils.validateToken(token, user)) {
                            return chain.filter(exchange);
                        }
                    }
                }
            }
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        };
    }

    @Bean
    public GatewayFilter currencyPermissionFilter() {
        return (exchange, chain) -> {
            String token = jwtUtils.extractTokenFromRequest(exchange);
            if (token != null) {
                if (gatewayService.isTokenRegistered(token)) {
                    String username = jwtUtils.extractUsername(token);
                    if (username != null && gatewayService.getUserDatabase().containsKey(username)) {
                        User user = gatewayService.getUserDatabase().get(username);
                        if (user != null && jwtUtils.validateToken(token, user)) {
                            if (exchange.getRequest().getPath().toString().contains("currencies")) {
                                String role = jwtUtils.extractRole(token);
                                if (role != null && role.equalsIgnoreCase("ADMIN")) {
                                    return chain.filter(exchange);
                                } else {
                                    HttpMethod method = exchange.getRequest().getMethod();
                                    if (method == HttpMethod.PUT || method == HttpMethod.POST || method == HttpMethod.DELETE) {
                                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                                        return exchange.getResponse().setComplete();
                                    } else {
                                        return chain.filter(exchange);
                                    }
                                }
                            } else {
                                return chain.filter(exchange);
                            }
                        }
                    }
                }
            }
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        };
    }
}
