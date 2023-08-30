package org.softtech.internship.backend.apigateway.controller;

import lombok.RequiredArgsConstructor;
import org.softtech.internship.backend.apigateway.config.GatewayConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gateway")
@RequiredArgsConstructor
public class GatewayController {
    private final GatewayConfig gatewayConfig;
    @GetMapping(path = "/refresh/users")
    public ResponseEntity<Void> getUsers() {
        gatewayConfig.refreshUsers();
        return ResponseEntity.ok().build();
    }
}
