package com.example.library.bookservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBookRequest {
    @NotBlank
    private String isbn;

    @NotBlank
    private String title;

    private String description;

    private Integer publicationYear;

    private String publisher;

    @NotNull
    private Integer totalCopies;

    @NotNull
    private Integer availableCopies;

    @NotBlank
    private String authorFirstName;

    @NotBlank
    private String authorLastName;

    private String authorBiography;

    @NotBlank
    private String categoryName;

    private String categoryDescription;
}
