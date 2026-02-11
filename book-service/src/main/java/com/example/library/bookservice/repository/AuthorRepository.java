package com.example.library.bookservice.repository;

import com.example.library.bookservice.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository JPA pour les auteurs.
 *
 * @since 1.0
 */
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
