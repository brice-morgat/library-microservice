package com.example.library.loanservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReturnRequest {
    private LocalDate returnDate;
}
