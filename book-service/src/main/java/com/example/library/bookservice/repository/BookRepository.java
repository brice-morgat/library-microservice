package com.example.library.bookservice.repository;

import com.example.library.bookservice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository JPA pour les livres.
 *
 * @since 1.0
 */
public interface BookRepository extends JpaRepository<Book, Long> {
    /**
     * Recherche un livre par ISBN.
     *
     * @param isbn ISBN.
     * @return livre eventuel.
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Recherche simple sur le titre ou la description.
     *
     * @param query terme de recherche.
     * @return liste de livres.
     */
    @Query("select b from Book b where lower(b.title) like lower(concat('%', :q, '%')) or lower(b.description) like lower(concat('%', :q, '%'))")
    List<Book> search(@Param("q") String query);
}
