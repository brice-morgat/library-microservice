package com.example.library.userservice.service;

import com.example.library.userservice.dto.LoginRequest;
import com.example.library.userservice.dto.RefreshTokenRequest;
import com.example.library.userservice.dto.RegisterRequest;
import com.example.library.userservice.dto.TokenResponse;
import com.example.library.userservice.exception.BadRequestException;
import com.example.library.userservice.model.User;
import com.example.library.userservice.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Service d'authentification (register/login/refresh).
 *
 * @since 1.0
 */
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager,
                       UserService userService,
                       JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * Inscription d'un utilisateur avec génération de token.
     *
     * @param request données d'inscription.
     * @return token JWT.
     */
    public TokenResponse register(RegisterRequest request) {
        User user = userService.register(request);
        String token = jwtService.generateToken(user);
        return TokenResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresAt(jwtService.getExpiration(token))
                .build();
    }

    /**
     * Authentifie un utilisateur et retourne un token.
     *
     * @param request données de connexion.
     * @return token JWT.
     */
    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userService.getByEmail(request.getEmail());
        String token = jwtService.generateToken(user);
        return TokenResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresAt(jwtService.getExpiration(token))
                .build();
    }

    /**
     * Rafraîchit un token JWT.
     *
     * @param request token à rafraîchir.
     * @return nouveau token JWT.
     */
    public TokenResponse refresh(RefreshTokenRequest request) {
        if (!jwtService.validateToken(request.getToken())) {
            throw new BadRequestException("Invalid token");
        }
        User user = userService.getByEmail(jwtService.getUsername(request.getToken()));
        String token = jwtService.generateToken(user);
        return TokenResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresAt(jwtService.getExpiration(token))
                .build();
    }
}
