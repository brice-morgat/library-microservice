package com.example.library.loanservice.client;

import com.example.library.loanservice.dto.BookDto;
import com.example.library.loanservice.dto.UpdateCopiesRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Client Feign vers le Book Service.
 *
 * @since 1.0
 */
@FeignClient(name = "book-service")
public interface BookClient {
    /**
     * Récupère un livre par id.
     *
     * @param id identifiant livre.
     * @return livre.
     */
    @GetMapping("/api/books/{id}")
    BookDto findById(@PathVariable("id") Long id);

    /**
     * Met à jour les copies d'un livre.
     *
     * @param id identifiant livre.
     * @param request mise à jour copies.
     * @return livre mis à jour.
     */
    @PatchMapping("/api/books/{id}/copies")
    BookDto updateCopies(@PathVariable("id") Long id, @RequestBody UpdateCopiesRequest request);
}
