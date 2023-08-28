package org.softtech.internship.backend.login.util;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.softtech.internship.backend.login.util.KeyUtils.loadPrivateKeyFromResource;
import static org.softtech.internship.backend.login.util.KeyUtils.loadPublicKeyFromResource;

public class JwtHandler {
    private static final Long EXPIRATION_TIME = 1000L * 60 * 60; // 1 hour

    private static String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private static Claims validateToken(String token) {
        try {
            PublicKey publicKey = loadPublicKeyFromResource("/public_key.pem");
            JwtParser parser = Jwts.parserBuilder().setSigningKey(publicKey).build();
            Jws<Claims> jws = parser.parseClaimsJws(token);
            return jws.getBody();
        } catch (JwtException e) {
            return null;
        }
    }

    public static String generateJwtToken(String userId, String username) {
        PrivateKey privateKey = loadPrivateKeyFromResource("/private_key.pem");
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(expiration).signWith(privateKey, SignatureAlgorithm.RS256).compact();
    }
}
