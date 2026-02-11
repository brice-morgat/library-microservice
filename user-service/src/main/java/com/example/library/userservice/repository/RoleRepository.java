package com.example.library.userservice.repository;

import com.example.library.userservice.model.Role;
import com.example.library.userservice.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository JPA pour les roles.
 *
 * @since 1.0
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Recherche un role par nom.
     *
     * @param name nom du role.
     * @return role eventuel.
     */
    Optional<Role> findByName(RoleName name);
}
