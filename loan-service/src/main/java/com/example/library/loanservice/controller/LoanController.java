package com.example.library.loanservice.controller;

import com.example.library.loanservice.dto.BorrowRequest;
import com.example.library.loanservice.dto.LoanDto;
import com.example.library.loanservice.dto.ReservationDto;
import com.example.library.loanservice.dto.ReturnRequest;
import com.example.library.loanservice.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des emprunts.
 *
 * @since 1.0
 */
@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    /**
     * Liste tous les emprunts.
     *
     * @return liste d'emprunts.
     */
    @GetMapping
    public ResponseEntity<List<LoanDto>> findAll() {
        return ResponseEntity.ok(loanService.findAll());
    }

    /**
     * Récupère un emprunt par id.
     *
     * @param id identifiant emprunt.
     * @return emprunt.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LoanDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.findById(id));
    }

    /**
     * Liste les emprunts d'un utilisateur.
     *
     * @param userId identifiant utilisateur.
     * @return liste d'emprunts.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanDto>> findByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(loanService.findByUserId(userId));
    }

    /**
     * Liste les emprunts d'un livre.
     *
     * @param bookId identifiant livre.
     * @return liste d'emprunts.
     */
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<LoanDto>> findByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(loanService.findByBookId(bookId));
    }

    /**
     * Crée un emprunt (borrow).
     *
     * @param request données d'emprunt.
     * @return emprunt.
     */
    @PostMapping("/borrow")
    public ResponseEntity<LoanDto> borrow(@Valid @RequestBody BorrowRequest request) {
        return ResponseEntity.ok(loanService.borrow(request));
    }

    /**
     * Retourne un livre (return).
     *
     * @param id identifiant emprunt.
     * @param request données de retour.
     * @return emprunt mis à jour.
     */
    @PostMapping("/{id}/return")
    public ResponseEntity<LoanDto> returnLoan(@PathVariable Long id, @RequestBody(required = false) ReturnRequest request) {
        return ResponseEntity.ok(loanService.returnLoan(id, request != null ? request.getReturnDate() : null));
    }

    /**
     * Cree une reservation de livre.
     *
     * @param request demande de reservation.
     * @return reservation creee.
     */
    @PostMapping("/reserve")
    public ResponseEntity<ReservationDto> reserve(@Valid @RequestBody BorrowRequest request) {
        return ResponseEntity.ok(loanService.reserve(request));
    }
}
