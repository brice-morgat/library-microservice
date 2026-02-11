package com.example.library.bookservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookDto {
    private Long id;
    private String isbn;
    private String title;
    private String description;
    private Integer publicationYear;
    private String publisher;
    private Integer totalCopies;
    private Integer availableCopies;
    private AuthorDto author;
    private CategoryDto category;
}
