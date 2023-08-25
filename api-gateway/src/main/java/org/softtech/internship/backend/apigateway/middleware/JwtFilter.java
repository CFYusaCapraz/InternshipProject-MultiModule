package org.softtech.internship.backend.apigateway.middleware;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.PublicKey;

import static org.softtech.internship.backend.apigateway.middleware.KeyUtils.loadPublicKeyFromResource;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = extractToken(request);

        if (token != null) {
            Claims claims = validateToken(token);

            if (claims != null) {
                request.setAttribute("claims", claims);
            }
        }

        chain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private Claims validateToken(String token) {
        try {
            PublicKey publicKey = loadPublicKeyFromResource("/public_key.pem");
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build();
            Jws<Claims> jws = parser.parseClaimsJws(token);
            return jws.getBody();
        } catch (JwtException e) {
            e.printStackTrace();
            return null;
        }
    }
}
