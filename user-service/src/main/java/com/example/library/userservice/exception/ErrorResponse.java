package com.example.library.userservice.exception;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

/**
 * Modele de reponse d'erreur.
 *
 * @since 1.0
 */
@Data
@Builder
public class ErrorResponse {
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
