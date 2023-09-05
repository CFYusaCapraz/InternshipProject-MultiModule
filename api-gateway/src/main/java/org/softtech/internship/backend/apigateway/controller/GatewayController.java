package org.softtech.internship.backend.apigateway.controller;

import lombok.RequiredArgsConstructor;
import org.softtech.internship.backend.apigateway.config.GatewayConfig;
import org.softtech.internship.backend.apigateway.service.GatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gateway")
@RequiredArgsConstructor
public class GatewayController {
    private final GatewayService gatewayService;

    @GetMapping(path = "/refresh/users")
    public ResponseEntity<Void> getUsers() {
        gatewayService.refreshUsers();
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/add/token")
    public ResponseEntity<Void> addToken(@RequestBody String token) {
        if (gatewayService.addToken(token))
            return ResponseEntity.ok().build();
        return ResponseEntity.internalServerError().build();
    }

    @PostMapping(path = "/remove/token")
    public ResponseEntity<Void> removeToken(@RequestBody String token) {
        if (gatewayService.removeToken(token))
            return ResponseEntity.ok().build();
        return ResponseEntity.internalServerError().build();
    }
}
