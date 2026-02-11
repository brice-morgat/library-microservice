package com.example.library.userservice.client;

import com.example.library.userservice.dto.LoanDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Client Feign pour le Loan Service.
 *
 * @since 1.0
 */
@FeignClient(name = "loan-service")
public interface LoanClient {
    /**
     * Retourne les emprunts d'un utilisateur.
     *
     * @param userId identifiant utilisateur.
     * @return liste d'emprunts.
     */
    @GetMapping("/api/loans/user/{userId}")
    List<LoanDto> findByUserId(@PathVariable("userId") Long userId);
}
