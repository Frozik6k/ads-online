package ru.skypro.homework.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${security.jwt.secret}")
    private String secrete;

    private SecretKey secretKey;

    @Value("${security.token.lifetime}")
    private Long expirationTime;

    @PostConstruct
    void init() {
        secretKey = generateSecretKey(secrete);
    }

    public String generateToken(UserDetails details) {
        return Jwts.builder()
            .subject(details.getUsername())
            .claim("role", details.getAuthorities())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact();
    }

    private SecretKey generateSecretKey(String secrete) {
        return Keys.hmacShaKeyFor(secrete.getBytes());
    }

    public String getUserName(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }


    public boolean validateToken(String token, UserDetails details) {
        return getUserName(token).equals(details.getUsername()) && !tokenExpired(token);
    }


    public boolean tokenExpired(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration()
            .before(new Date());
    }
}
