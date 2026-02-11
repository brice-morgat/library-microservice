package com.example.library.apigateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;

/**
 * Service utilitaire pour valider et lire les jetons JWT dans la gateway.
 *
 * @since 1.0
 */
@Service
public class JwtService {

    private final String secret;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    /**
     * Valide la signature et l'expiration du token.
     *
     * @param token JWT brut.
     * @return true si le token est valide.
     */
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Extrait l'identifiant utilisateur (subject) du token.
     *
     * @param token JWT brut.
     * @return email/subject.
     */
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extrait les rôles portés par le token.
     *
     * @param token JWT brut.
     * @return liste de rôles (ex: ADMIN, USER).
     */
    public List<String> getRoles(String token) {
        Object value = getClaims(token).get("roles");
        if (value instanceof List<?> list) {
            return list.stream().map(String::valueOf).toList();
        }
        return List.of();
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
