package com.example.library.bookservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String biography;
}
