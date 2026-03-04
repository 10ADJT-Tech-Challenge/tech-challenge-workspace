package br.com.fiap.tech.challenge.authservice.infrastructure.security;

import br.com.fiap.tech.challenge.authservice.domain.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {

    private final String issuer;
    private final long expirationSeconds;
    private final SecretKey key;

    public JwtService(
            @Value("${app.jwt.issuer}") String issuer,
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-seconds}") long expirationSeconds
    ) {
        this.issuer = issuer;
        this.expirationSeconds = expirationSeconds;
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UUID userId, String email, UserRole role) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expirationSeconds);

        // roles no formato Spring Security
        List<String> roles = List.of("ROLE_" + role.name());

        return Jwts.builder()
                .issuer(issuer)
                .subject(String.valueOf(userId))     // sub = id do cliente (exigência do desafio)
                .claims(Map.of(
                        "email", email,
                        "roles", roles
                ))
                .issuedAt(java.util.Date.from(now))
                .expiration(java.util.Date.from(exp))
                .signWith(key)
                .compact();
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }
}
