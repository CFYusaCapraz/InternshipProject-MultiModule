package org.softtech.internship.backend.login.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.softtech.internship.backend.login.model.user.User;
import org.springframework.lang.Nullable;

import java.security.PrivateKey;
import java.util.Date;
import java.util.Map;

import static org.softtech.internship.backend.login.util.KeyUtils.loadPrivateKeyFromResource;

public class JwtUtils {
    private static final Long EXPIRATION_TIME = 1000L * 60 * 60; // 1 hour

    public String generateJwtToken(User userDetails, @Nullable String userId, @Nullable Map<String, ?> extraClaims) {
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
