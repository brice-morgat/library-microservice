package com.example.library.userservice.security;

import com.example.library.userservice.model.RoleName;
import com.example.library.userservice.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Service JWT du User Service pour générer et valider les tokens.
 *
 * @since 1.0
 */
@Service
public class JwtService {

    private final String secret;
    private final long expirationMillis;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMillis
    ) {
        this.secret = secret;
        this.expirationMillis = expirationMillis;
    }

    /**
     * Génère un token JWT à partir d'un utilisateur.
     *
     * @param user utilisateur source.
     * @return token JWT.
     */
    public String generateToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(expirationMillis);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .claim("userId", user.getId())
                .claim("roles", user.getRoles().stream().map(r -> r.getName().name()).toList())
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Extrait l'email (subject) du token.
     *
     * @param token JWT brut.
     * @return email.
     */
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extrait l'identifiant utilisateur du token.
     *
     * @param token JWT brut.
     * @return id utilisateur ou null.
     */
    public Long getUserId(String token) {
        Object value = getClaims(token).get("userId");
        if (value instanceof Number number) {
            return number.longValue();
        }
        return null;
    }

    /**
     * Extrait les rôles portés par le token.
     *
     * @param token JWT brut.
     * @return ensemble de rôles.
     */
    public Set<RoleName> getRoles(String token) {
        Object value = getClaims(token).get("roles");
        if (value instanceof List<?> list) {
            return list.stream()
                    .map(String::valueOf)
                    .map(RoleName::valueOf)
                    .collect(java.util.stream.Collectors.toSet());
        }
        return Set.of();
    }

    /**
     * Retourne la date d'expiration du token.
     *
     * @param token JWT brut.
     * @return instant d'expiration.
     */
    public Instant getExpiration(String token) {
        return getClaims(token).getExpiration().toInstant();
    }

    /**
     * Valide la signature et l'expiration du token.
     *
     * @param token JWT brut.
     * @return true si valide.
     */
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
