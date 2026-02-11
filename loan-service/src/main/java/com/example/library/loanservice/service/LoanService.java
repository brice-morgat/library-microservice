package com.example.library.loanservice.service;

import com.example.library.loanservice.client.BookClient;
import com.example.library.loanservice.client.UserClient;
import com.example.library.loanservice.dto.BookDto;
import com.example.library.loanservice.dto.BorrowRequest;
import com.example.library.loanservice.dto.LoanDto;
import com.example.library.loanservice.dto.ReservationDto;
import com.example.library.loanservice.dto.UpdateCopiesRequest;
import com.example.library.loanservice.exception.BookNotAvailableException;
import com.example.library.loanservice.exception.NotFoundException;
import com.example.library.loanservice.mapper.LoanMapper;
import com.example.library.loanservice.mapper.ReservationMapper;
import com.example.library.loanservice.model.Loan;
import com.example.library.loanservice.model.LoanStatus;
import com.example.library.loanservice.model.Reservation;
import com.example.library.loanservice.model.ReservationStatus;
import com.example.library.loanservice.repository.LoanRepository;
import com.example.library.loanservice.repository.ReservationRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service métier pour la gestion des emprunts.
 *
 * @since 1.0
 */
@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookClient bookClient;
    private final UserClient userClient;
    private final ReservationRepository reservationRepository;

    public LoanService(LoanRepository loanRepository,
                       BookClient bookClient,
                       UserClient userClient,
                       ReservationRepository reservationRepository) {
        this.loanRepository = loanRepository;
        this.bookClient = bookClient;
        this.userClient = userClient;
        this.reservationRepository = reservationRepository;
    }

    /**
     * Crée un emprunt en appelant User Service et Book Service.
     *
     * <p>Protégé par CircuitBreaker/Retry via Resilience4j.</p>
     *
     * @param request demande d'emprunt.
     * @return emprunt créé.
     */
    @CircuitBreaker(name = "bookService", fallbackMethod = "borrowFallback")
    @Retry(name = "bookService", fallbackMethod = "borrowFallback")
    public LoanDto borrow(BorrowRequest request) {
        userClient.findById(request.getUserId());

        BookDto book = bookClient.findById(request.getBookId());
        if (book.getAvailableCopies() == null || book.getAvailableCopies() < 1) {
            throw new BookNotAvailableException(request.getBookId());
        }

        Loan loan = Loan.builder()
                .userId(request.getUserId())
                .bookId(request.getBookId())
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusWeeks(3))
                .status(LoanStatus.ACTIVE)
                .build();

        UpdateCopiesRequest update = new UpdateCopiesRequest();
        update.setDeltaAvailable(-1);
        bookClient.updateCopies(request.getBookId(), update);

        return LoanMapper.toDto(loanRepository.save(loan));
    }

    /**
     * Fallback en cas d'indisponibilité du Book Service.
     *
     * @param request demande d'emprunt.
     * @param ex exception déclenchée.
     * @return emprunt en statut PENDING.
     */
    public LoanDto borrowFallback(BorrowRequest request, Throwable ex) {
        return LoanDto.builder()
                .userId(request.getUserId())
                .bookId(request.getBookId())
                .status(LoanStatus.PENDING.name())
                .message("Service temporarily unavailable")
                .build();
    }

    /**
     * Retourne un emprunt et incrémente les copies du livre.
     *
     * @param id identifiant emprunt.
     * @param returnDate date de retour (optionnelle).
     * @return emprunt mis à jour.
     */
    public LoanDto returnLoan(Long id, LocalDate returnDate) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Loan not found"));
        if (loan.getStatus() == LoanStatus.RETURNED) {
            return LoanMapper.toDto(loan);
        }
        loan.setReturnDate(returnDate != null ? returnDate : LocalDate.now());
        loan.setStatus(LoanStatus.RETURNED);

        UpdateCopiesRequest update = new UpdateCopiesRequest();
        update.setDeltaAvailable(1);
        bookClient.updateCopies(loan.getBookId(), update);

        return LoanMapper.toDto(loanRepository.save(loan));
    }

    /**
     * Cree une reservation de livre.
     *
     * @param request demande de reservation.
     * @return reservation creee.
     */
    public ReservationDto reserve(BorrowRequest request) {
        userClient.findById(request.getUserId());
        bookClient.findById(request.getBookId());

        Reservation reservation = Reservation.builder()
                .userId(request.getUserId())
                .bookId(request.getBookId())
                .createdAt(LocalDate.now())
                .status(ReservationStatus.REQUESTED)
                .build();

        return ReservationMapper.toDto(reservationRepository.save(reservation));
    }

    /**
     * Liste tous les emprunts.
     *
     * @return liste d'emprunts.
     */
    public List<LoanDto> findAll() {
        return loanRepository.findAll().stream()
                .map(LoanMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère un emprunt par id.
     *
     * @param id identifiant emprunt.
     * @return emprunt.
     */
    public LoanDto findById(Long id) {
        return LoanMapper.toDto(loanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Loan not found")));
    }

    /**
     * Liste les emprunts d'un utilisateur.
     *
     * @param userId identifiant utilisateur.
     * @return liste d'emprunts.
     */
    public List<LoanDto> findByUserId(Long userId) {
        return loanRepository.findByUserId(userId).stream()
                .map(LoanMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Liste les emprunts d'un livre.
     *
     * @param bookId identifiant livre.
     * @return liste d'emprunts.
     */
    public List<LoanDto> findByBookId(Long bookId) {
        return loanRepository.findByBookId(bookId).stream()
                .map(LoanMapper::toDto)
                .collect(Collectors.toList());
    }
}
