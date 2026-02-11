package com.example.library.userservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanDto {
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;
    private String message;
}
