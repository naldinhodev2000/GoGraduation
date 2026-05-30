package fafenterprise.dev.gograduation.services;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import fafenterprise.dev.gograduation.entity.uno.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm; // (pode remover se estiver dando conflito)
@Service
public class JwtService {

    private static final String SECRET =
            "your-very-strong-secret-key-must-be-at-least-32-chars";

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserEntity user) {
        return Jwts.builder()
                .subject(user.getId().toString())

                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .claim("login", user.getLogin())

                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))

                .signWith(getSigningKey())
                .compact();
    }
}