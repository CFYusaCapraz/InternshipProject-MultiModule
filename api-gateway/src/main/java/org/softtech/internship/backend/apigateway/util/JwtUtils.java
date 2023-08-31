package org.softtech.internship.backend.apigateway.util;

import io.jsonwebtoken.*;
import org.softtech.internship.backend.apigateway.model.User;
import org.springframework.web.server.ServerWebExchange;

import java.security.PublicKey;
import java.util.Date;
import java.util.function.Function;

import static org.softtech.internship.backend.apigateway.util.KeyUtils.loadPublicKeyFromResource;

public class JwtUtils {

    public String extractTokenFromRequest(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    public boolean validateToken(String token, User user) {
        String username = extractUsername(token);
        String password = extractPassword(token);
        String role = extractRole(token);
        return username.equals(user.getUsername())
                && password.equals(user.getPassword())
                && role.equals(user.getRole())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        Claims claims = getClaims(token);
        return claims.get("username", String.class);
    }

    public String extractPassword(String token) {
        Claims claims = getClaims(token);
        return claims.get("password", String.class);
    }

    public String extractRole(String token) {
        Claims claims = getClaims(token);
        return claims.get("role", String.class);
    }

    public Claims getClaims(String token) {
        try {
            PublicKey publicKey = loadPublicKeyFromResource("/public_key.pem");
            JwtParser parser = Jwts.parserBuilder().setSigningKey(publicKey).build();
            Jws<Claims> jws = parser.parseClaimsJws(token);
            return jws.getBody();
        } catch (JwtException e) {
            return null;
        }
    }

}
