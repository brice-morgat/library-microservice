package com.example.library.bookservice.repository;

import com.example.library.bookservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository JPA pour les categories.
 *
 * @since 1.0
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Recherche une categorie par nom (insensible a la casse).
     *
     * @param name nom de categorie.
     * @return categorie eventuelle.
     */
    Optional<Category> findByNameIgnoreCase(String name);
}
