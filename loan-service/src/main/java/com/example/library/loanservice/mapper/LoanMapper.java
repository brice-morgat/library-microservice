package com.example.library.loanservice.mapper;

import com.example.library.loanservice.dto.LoanDto;
import com.example.library.loanservice.model.Loan;

public final class LoanMapper {
    private LoanMapper() {}

    public static LoanDto toDto(Loan loan) {
        if (loan == null) {
            return null;
        }
        return LoanDto.builder()
                .id(loan.getId())
                .userId(loan.getUserId())
                .bookId(loan.getBookId())
                .borrowDate(loan.getBorrowDate())
                .dueDate(loan.getDueDate())
                .returnDate(loan.getReturnDate())
                .status(loan.getStatus().name())
                .build();
    }
}
