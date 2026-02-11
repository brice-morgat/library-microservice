package com.example.library.bookservice.mapper;

import com.example.library.bookservice.dto.AuthorDto;
import com.example.library.bookservice.dto.BookDto;
import com.example.library.bookservice.dto.CategoryDto;
import com.example.library.bookservice.model.Author;
import com.example.library.bookservice.model.Book;
import com.example.library.bookservice.model.Category;

public final class BookMapper {
    private BookMapper() {}

    public static BookDto toDto(Book book) {
        if (book == null) {
            return null;
        }
        return BookDto.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .description(book.getDescription())
                .publicationYear(book.getPublicationYear())
                .publisher(book.getPublisher())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .author(toDto(book.getAuthor()))
                .category(toDto(book.getCategory()))
                .build();
    }

    public static AuthorDto toDto(Author author) {
        if (author == null) {
            return null;
        }
        return AuthorDto.builder()
                .id(author.getId())
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .biography(author.getBiography())
                .build();
    }

    public static CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
