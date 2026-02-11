package com.example.library.loanservice.dto;

import lombok.Data;

@Data
public class UpdateCopiesRequest {
    private Integer totalCopies;
    private Integer availableCopies;
    private Integer deltaTotal;
    private Integer deltaAvailable;
}
