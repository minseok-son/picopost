package com.example.picopost.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.example.picopost.auth.model.User;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    
    // WARNING: Replace with a secure key loaded from application.properties/environment
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512); 
    private final long expirationMs = 86400000; // 24 hours

    /**
     * Generates a JWT based on the authenticated user.
     */
    public String generateToken(Authentication authentication) {
        // Here we store the user's ID as a subject. 
        // This is the Long userId you will retrieve later.
        String userId = ((User) authentication.getPrincipal()).getId().toString();

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Validates the integrity of the JWT.
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            // Log specific JWT exceptions (expired, signature mismatch, etc.)
            System.err.println("JWT Validation Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves the user ID (subject) from the JWT.
     */
    public Long getUserIdFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }
}