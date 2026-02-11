package com.example.library.userservice.controller;

import com.example.library.userservice.dto.LoginRequest;
import com.example.library.userservice.dto.RefreshTokenRequest;
import com.example.library.userservice.dto.RegisterRequest;
import com.example.library.userservice.dto.TokenResponse;
import com.example.library.userservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur d'authentification (JWT).
 *
 * @since 1.0
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Enregistre un nouvel utilisateur et retourne un token.
     *
     * @param request données d'inscription.
     * @return token JWT.
     */
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Authentifie un utilisateur et retourne un token.
     *
     * @param request données de connexion.
     * @return token JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * Rafraîchit un token JWT.
     *
     * @param request token à rafraîchir.
     * @return nouveau token JWT.
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }
}
