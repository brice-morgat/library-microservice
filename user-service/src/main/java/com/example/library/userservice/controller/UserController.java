package com.example.library.userservice.controller;

import com.example.library.userservice.client.LoanClient;
import com.example.library.userservice.dto.CreateUserRequest;
import com.example.library.userservice.dto.LoanDto;
import com.example.library.userservice.dto.UpdateUserRequest;
import com.example.library.userservice.dto.UserDto;
import com.example.library.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des utilisateurs.
 *
 * @since 1.0
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final LoanClient loanClient;

    public UserController(UserService userService, LoanClient loanClient) {
        this.userService = userService;
        this.loanClient = loanClient;
    }

    /**
     * Liste tous les utilisateurs (ADMIN).
     *
     * @return liste d'utilisateurs.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    /**
     * Récupère un utilisateur par id (ADMIN ou soi-même).
     *
     * @param id identifiant utilisateur.
     * @param authentication contexte de sécurité.
     * @return utilisateur.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(userService.findById(id, authentication));
    }

    /**
     * Crée un utilisateur (ADMIN).
     *
     * @param request données de création.
     * @return utilisateur créé.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> create(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.create(request));
    }

    /**
     * Met à jour un utilisateur (ADMIN ou soi-même).
     *
     * @param id identifiant utilisateur.
     * @param request données de mise à jour.
     * @param authentication contexte de sécurité.
     * @return utilisateur mis à jour.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id,
                                          @Valid @RequestBody UpdateUserRequest request,
                                          Authentication authentication) {
        return ResponseEntity.ok(userService.update(id, request, authentication));
    }

    /**
     * Supprime un utilisateur (ADMIN).
     *
     * @param id identifiant utilisateur.
     * @return réponse 204.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Liste les emprunts d'un utilisateur.
     *
     * @param id identifiant utilisateur.
     * @param authentication contexte de sécurité.
     * @return liste d'emprunts.
     */
    @GetMapping("/{id}/loans")
    public ResponseEntity<List<LoanDto>> getLoans(@PathVariable Long id, Authentication authentication) {
        userService.findById(id, authentication);
        return ResponseEntity.ok(loanClient.findByUserId(id));
    }
}
