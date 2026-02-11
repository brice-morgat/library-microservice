package com.example.library.userservice.exception;

/**
 * Exception levee pour une requete invalide.
 *
 * @since 1.0
 */
public class BadRequestException extends RuntimeException {
    /**
     * Construit l'exception avec un message explicite.
     *
     * @param message description de l'erreur.
     */
    public BadRequestException(String message) {
        super(message);
    }
}
