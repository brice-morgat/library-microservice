package com.example.library.loanservice.repository;

import com.example.library.loanservice.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository JPA pour les emprunts.
 *
 * @since 1.0
 */
public interface LoanRepository extends JpaRepository<Loan, Long> {
    /**
     * Liste les emprunts d'un utilisateur.
     *
     * @param userId identifiant utilisateur.
     * @return liste d'emprunts.
     */
    List<Loan> findByUserId(Long userId);

    /**
     * Liste les emprunts d'un livre.
     *
     * @param bookId identifiant livre.
     * @return liste d'emprunts.
     */
    List<Loan> findByBookId(Long bookId);
}
