package com.example.library.userservice.repository;

import com.example.library.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository JPA pour les utilisateurs.
 *
 * @since 1.0
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Recherche un utilisateur par email.
     *
     * @param email email utilisateur.
     * @return utilisateur eventuel.
     */
    Optional<User> findByEmail(String email);

    /**
     * Indique si un email existe deja.
     *
     * @param email email a verifier.
     * @return true si present.
     */
    boolean existsByEmail(String email);

    /**
     * Indique si un numero d'adhesion existe deja.
     *
     * @param membershipNumber numero d'adhesion.
     * @return true si present.
     */
    boolean existsByMembershipNumber(String membershipNumber);
}
