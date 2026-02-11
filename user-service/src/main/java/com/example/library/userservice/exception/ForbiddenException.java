package com.example.library.userservice.exception;

/**
 * Exception levee pour un acces interdit.
 *
 * @since 1.0
 */
public class ForbiddenException extends RuntimeException {
    /**
     * Construit l'exception avec un message explicite.
     *
     * @param message description de l'erreur.
     */
    public ForbiddenException(String message) {
        super(message);
    }
}
