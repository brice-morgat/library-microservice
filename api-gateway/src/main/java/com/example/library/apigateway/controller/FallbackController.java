package com.example.library.apigateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Endpoints de secours exposés par la gateway pour les services indisponibles.
 *
 * @since 1.0
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    /**
     * Fallback pour le User Service.
     *
     * @return réponse HTTP 503 avec un message explicite.
     */
    @GetMapping("/users")
    public ResponseEntity<Map<String, String>> usersFallback() {
        return ResponseEntity.status(503).body(Map.of("message", "User service unavailable"));
    }

    /**
     * Fallback pour le Book Service.
     *
     * @return réponse HTTP 503 avec un message explicite.
     */
    @GetMapping("/books")
    public ResponseEntity<Map<String, String>> booksFallback() {
        return ResponseEntity.status(503).body(Map.of("message", "Book service unavailable"));
    }

    /**
     * Fallback pour le Loan Service.
     *
     * @return réponse HTTP 503 avec un message explicite.
     */
    @GetMapping("/loans")
    public ResponseEntity<Map<String, String>> loansFallback() {
        return ResponseEntity.status(503).body(Map.of("message", "Loan service unavailable"));
    }
}
