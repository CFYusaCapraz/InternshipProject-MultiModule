package org.softtech.internship.backend.login.util;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import static org.softtech.internship.backend.login.util.KeyUtils.loadPrivateKeyFromResource;
import static org.softtech.internship.backend.login.util.KeyUtils.loadPublicKeyFromResource;

public class JwtHandler {
    private static final Long EXPIRATION_TIME = 1000L * 60 * 60; // 1 hour

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String extractUsername(String token) {
        Claims claims = getClaims(token);
        return (String) claims.get("username");
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
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

    public String generateJwtToken(UserDetails userDetails, @Nullable String userId, @Nullable Map<String, ?> extraClaims) {
        PrivateKey privateKey = loadPrivateKeyFromResource("/private_key.pem");

        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);
        extraClaims = extraClaims == null ? Map.of() : extraClaims;

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setId(userId)
                .setClaims(extraClaims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

}
