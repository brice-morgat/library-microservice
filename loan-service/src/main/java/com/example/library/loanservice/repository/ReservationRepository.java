package com.example.library.loanservice.repository;

import com.example.library.loanservice.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository JPA pour les reservations.
 *
 * @since 1.0
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
