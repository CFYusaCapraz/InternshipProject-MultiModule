package org.softtech.internship.backend.apigateway.controller;

import lombok.RequiredArgsConstructor;
import org.softtech.internship.backend.apigateway.config.GatewayConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(path = "/add/token")
    public ResponseEntity<Void> addToken(@RequestBody String token) {
        if (gatewayConfig.addToken(token))
            return ResponseEntity.ok().build();
        return ResponseEntity.internalServerError().build();
    }
}
