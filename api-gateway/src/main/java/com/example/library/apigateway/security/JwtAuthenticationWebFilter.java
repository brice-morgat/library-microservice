package com.example.library.apigateway.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Filtre WebFlux chargé d'authentifier les requêtes via JWT.
 *
 * @since 1.0
 */
@Component
public class JwtAuthenticationWebFilter implements WebFilter {

    private final JwtService jwtService;

    public JwtAuthenticationWebFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Filtre les requêtes entrantes et place l'authentification dans le contexte réactif.
     *
     * @param exchange échange HTTP réactif.
     * @param chain chaîne de filtres.
     * @return Mono de fin de traitement.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            return unauthorized(exchange.getResponse());
        }

        String token = header.substring(7);
        if (!jwtService.validateToken(token)) {
            return unauthorized(exchange.getResponse());
        }

        Set<SimpleGrantedAuthority> authorities = jwtService.getRoles(token).stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                jwtService.getUsername(token),
                null,
                authorities
        );

        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
    }

    /**
     * Indique si l'URI est publique.
     *
     * @param path chemin de la requête.
     * @return true si route publique.
     */
    private boolean isPublicPath(String path) {
        return path.startsWith("/api/auth/")
                || path.startsWith("/actuator/")
                || path.startsWith("/fallback/");
    }

    /**
     * Retourne une réponse 401.
     *
     * @param response réponse HTTP.
     * @return Mono completé avec statut 401.
     */
    private Mono<Void> unauthorized(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
}
