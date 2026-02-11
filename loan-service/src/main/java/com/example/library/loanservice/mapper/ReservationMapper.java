package com.example.library.loanservice.mapper;

import com.example.library.loanservice.dto.ReservationDto;
import com.example.library.loanservice.model.Reservation;

/**
 * Mapper pour les reservations.
 *
 * @since 1.0
 */
public final class ReservationMapper {
    private ReservationMapper() {}

    public static ReservationDto toDto(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        return ReservationDto.builder()
                .id(reservation.getId())
                .userId(reservation.getUserId())
                .bookId(reservation.getBookId())
                .createdAt(reservation.getCreatedAt())
                .status(reservation.getStatus().name())
                .build();
    }
}
