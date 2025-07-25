package com.hotel.hotelmanagementsystem.service;

import com.hotel.hotelmanagementsystem.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    // Secret key for JWT signing. This should be a strong, randomly generated key
    // and ideally stored securely (e.g., environment variable, Vault).
    // For local development, a hardcoded string is okay, but ensure it's long enough.
    @Value("${jwt.secret:thisismysecretkeyforjwtauthenticationanditshouldbelongenoughforsecuritypurposes}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // 24 hours in milliseconds
    private long expirationMs;

    private SecretKey key;

    // Initialize the secret key from the @Value string
    public AuthService(@Value("${jwt.secret:thisismysecretkeyforjwtauthenticationanditshouldbelongenoughforsecuritypurposes}") String secret) {
        this.secret = secret;
        // Ensure the secret key is sufficiently long for HS256 (at least 256 bits or 32 bytes)
        // If the provided secret string is too short, generate a new secure key.
        if (secret.length() < 32) { // 32 bytes = 256 bits
            this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            System.out.println("WARNING: JWT secret is too short. Generating a new random key. For production, use a strong, fixed secret.");
        } else {
            this.key = Keys.hmacShaKeyFor(secret.getBytes());
        }
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("name", user.getName());
        claims.put("role", user.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail()) // Subject of the token
                .setIssuedAt(new Date(System.currentTimeMillis())) // When the token was issued
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs)) // Token expiration time
                .signWith(key, SignatureAlgorithm.HS256) // Sign the token with the secret key and algorithm
                .compact(); // Build and compact the JWT to a string
    }

    // You can add a validateToken method here if you were implementing full Spring Security
    // For this setup, we're just generating tokens for the frontend to store.
}
