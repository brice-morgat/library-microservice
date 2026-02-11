package com.example.library.bookservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateBookRequest {
    @NotBlank
    private String title;

    private String description;

    private Integer publicationYear;

    private String publisher;

    private Integer totalCopies;

    private Integer availableCopies;

    private String authorFirstName;

    private String authorLastName;

    private String authorBiography;

    private String categoryName;

    private String categoryDescription;
}
