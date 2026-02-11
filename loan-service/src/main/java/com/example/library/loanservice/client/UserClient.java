package com.example.library.loanservice.client;

import com.example.library.loanservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Client Feign vers le User Service.
 *
 * @since 1.0
 */
@FeignClient(name = "user-service")
public interface UserClient {
    /**
     * Récupère un utilisateur par id.
     *
     * @param id identifiant utilisateur.
     * @return utilisateur.
     */
    @GetMapping("/api/users/{id}")
    UserDto findById(@PathVariable("id") Long id);
}
