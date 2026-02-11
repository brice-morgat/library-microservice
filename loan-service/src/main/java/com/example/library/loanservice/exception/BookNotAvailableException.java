package com.example.library.loanservice.exception;

/**
 * Exception levee quand un livre n'est pas disponible.
 *
 * @since 1.0
 */
public class BookNotAvailableException extends RuntimeException {
    /**
     * Construit l'exception pour un livre indisponible.
     *
     * @param bookId identifiant du livre.
     */
    public BookNotAvailableException(Long bookId) {
        super("Book not available: " + bookId);
    }
}
