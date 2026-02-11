package com.example.library.loanservice.dto;

import lombok.Data;

@Data
public class BookDto {
    private Long id;
    private String title;
    private Integer totalCopies;
    private Integer availableCopies;
}
