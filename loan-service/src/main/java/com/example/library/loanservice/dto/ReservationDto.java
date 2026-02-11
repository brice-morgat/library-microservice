package com.example.library.loanservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO pour les reservations de livres.
 *
 * @since 1.0
 */
@Data
@Builder
public class ReservationDto {
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDate createdAt;
    private String status;
}
