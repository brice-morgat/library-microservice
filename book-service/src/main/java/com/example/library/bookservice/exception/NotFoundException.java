package com.example.library.bookservice.exception;

/**
 * Exception levee quand une ressource n'est pas trouvee.
 *
 * @since 1.0
 */
public class NotFoundException extends RuntimeException {
    /**
     * Construit l'exception avec un message explicite.
     *
     * @param message description de l'erreur.
     */
    public NotFoundException(String message) {
        super(message);
    }
}
