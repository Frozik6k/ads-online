package ru.skypro.homework.security;

import java.time.Clock;
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
    private String secret;

    private SecretKey secretKey;

    @Value("${security.token.lifetime}")
    private Long expirationTime;

    private final Clock clock;

    @PostConstruct
    void init() {
        secretKey = generateSecretKey(secret);
    }

    public String generateToken(UserDetails details) {

        Date now = Date.from(clock.instant());
        Date exp = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
            .subject(details.getUsername())
            .claim("role", details.getAuthorities())
            .issuedAt(now)
            .expiration(exp)
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact();
    }

    private SecretKey generateSecretKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes());
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
