package com.example.library.bookservice.controller;

import com.example.library.bookservice.dto.BookDto;
import com.example.library.bookservice.dto.CreateBookRequest;
import com.example.library.bookservice.dto.UpdateBookRequest;
import com.example.library.bookservice.dto.UpdateCopiesRequest;
import com.example.library.bookservice.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contrôleur REST pour le catalogue de livres.
 *
 * @since 1.0
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Liste tous les livres.
     *
     * @return liste des livres.
     */
    @GetMapping
    public ResponseEntity<List<BookDto>> findAll() {
        return ResponseEntity.ok(bookService.findAll());
    }

    /**
     * Récupère un livre par id.
     *
     * @param id identifiant livre.
     * @return livre.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    /**
     * Récupère un livre par ISBN.
     *
     * @param isbn ISBN du livre.
     * @return livre.
     */
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDto> findByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(bookService.findByIsbn(isbn));
    }

    /**
     * Recherche full-text sur le titre/description.
     *
     * @param query terme de recherche.
     * @return résultats.
     */
    @GetMapping("/search")
    public ResponseEntity<List<BookDto>> search(@RequestParam("q") String query) {
        return ResponseEntity.ok(bookService.search(query));
    }

    /**
     * Crée un livre (ADMIN/LIBRARIAN).
     *
     * @param request données de création.
     * @return livre créé.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<BookDto> create(@Valid @RequestBody CreateBookRequest request) {
        return ResponseEntity.ok(bookService.create(request));
    }

    /**
     * Met à jour un livre (ADMIN/LIBRARIAN).
     *
     * @param id identifiant livre.
     * @param request données de mise à jour.
     * @return livre mis à jour.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<BookDto> update(@PathVariable Long id, @Valid @RequestBody UpdateBookRequest request) {
        return ResponseEntity.ok(bookService.update(id, request));
    }

    /**
     * Supprime un livre (ADMIN/LIBRARIAN).
     *
     * @param id identifiant livre.
     * @return réponse 204.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Met à jour le stock de copies.
     *
     * @param id identifiant livre.
     * @param request mises à jour.
     * @return livre mis à jour.
     */
    @PatchMapping("/{id}/copies")
    public ResponseEntity<BookDto> updateCopies(@PathVariable Long id, @RequestBody UpdateCopiesRequest request) {
        return ResponseEntity.ok(bookService.updateCopies(id, request));
    }
}
