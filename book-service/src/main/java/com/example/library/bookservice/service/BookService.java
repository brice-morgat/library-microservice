package com.example.library.bookservice.service;

import com.example.library.bookservice.dto.BookDto;
import com.example.library.bookservice.dto.CreateBookRequest;
import com.example.library.bookservice.dto.UpdateBookRequest;
import com.example.library.bookservice.dto.UpdateCopiesRequest;
import com.example.library.bookservice.exception.BadRequestException;
import com.example.library.bookservice.exception.NotFoundException;
import com.example.library.bookservice.mapper.BookMapper;
import com.example.library.bookservice.model.Author;
import com.example.library.bookservice.model.Book;
import com.example.library.bookservice.model.Category;
import com.example.library.bookservice.repository.AuthorRepository;
import com.example.library.bookservice.repository.BookRepository;
import com.example.library.bookservice.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service métier pour la gestion du catalogue de livres.
 *
 * @since 1.0
 */
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    public BookService(BookRepository bookRepository,
                       AuthorRepository authorRepository,
                       CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Retourne tous les livres.
     *
     * @return liste de livres.
     */
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(BookMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retourne un livre par id.
     *
     * @param id identifiant livre.
     * @return livre.
     */
    public BookDto findById(Long id) {
        return BookMapper.toDto(getBook(id));
    }

    /**
     * Retourne un livre par ISBN.
     *
     * @param isbn ISBN.
     * @return livre.
     */
    public BookDto findByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new NotFoundException("Book not found"));
        return BookMapper.toDto(book);
    }

    /**
     * Recherche full-text dans le catalogue.
     *
     * @param query terme de recherche.
     * @return résultats.
     */
    public List<BookDto> search(String query) {
        return bookRepository.search(query).stream()
                .map(BookMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Crée un livre.
     *
     * @param request données de création.
     * @return livre créé.
     */
    public BookDto create(CreateBookRequest request) {
        if (bookRepository.findByIsbn(request.getIsbn()).isPresent()) {
            throw new BadRequestException("ISBN already exists");
        }
        if (request.getTotalCopies() < 0 || request.getAvailableCopies() < 0) {
            throw new BadRequestException("Copies cannot be negative");
        }
        if (request.getAvailableCopies() > request.getTotalCopies()) {
            throw new BadRequestException("Available copies cannot exceed total copies");
        }

        Author author = authorRepository.save(Author.builder()
                .firstName(request.getAuthorFirstName())
                .lastName(request.getAuthorLastName())
                .biography(request.getAuthorBiography())
                .build());

        Category category = resolveCategory(request.getCategoryName(), request.getCategoryDescription());

        Book book = Book.builder()
                .isbn(request.getIsbn())
                .title(request.getTitle())
                .description(request.getDescription())
                .publicationYear(request.getPublicationYear())
                .publisher(request.getPublisher())
                .totalCopies(request.getTotalCopies())
                .availableCopies(request.getAvailableCopies())
                .author(author)
                .category(category)
                .build();

        return BookMapper.toDto(bookRepository.save(book));
    }

    /**
     * Met à jour un livre.
     *
     * @param id identifiant livre.
     * @param request données de mise à jour.
     * @return livre mis à jour.
     */
    public BookDto update(Long id, UpdateBookRequest request) {
        Book book = getBook(id);

        book.setTitle(request.getTitle());
        book.setDescription(request.getDescription());
        book.setPublicationYear(request.getPublicationYear());
        book.setPublisher(request.getPublisher());

        if (request.getTotalCopies() != null) {
            book.setTotalCopies(request.getTotalCopies());
        }
        if (request.getAvailableCopies() != null) {
            book.setAvailableCopies(request.getAvailableCopies());
        }

        if (request.getAuthorFirstName() != null || request.getAuthorLastName() != null) {
            Author author = book.getAuthor();
            if (author == null) {
                author = new Author();
            }
            if (request.getAuthorFirstName() != null) {
                author.setFirstName(request.getAuthorFirstName());
            }
            if (request.getAuthorLastName() != null) {
                author.setLastName(request.getAuthorLastName());
            }
            if (request.getAuthorBiography() != null) {
                author.setBiography(request.getAuthorBiography());
            }
            book.setAuthor(authorRepository.save(author));
        }

        if (request.getCategoryName() != null) {
            Category category = resolveCategory(request.getCategoryName(), request.getCategoryDescription());
            book.setCategory(category);
        }

        if (book.getTotalCopies() < 0 || book.getAvailableCopies() < 0) {
            throw new BadRequestException("Copies cannot be negative");
        }
        if (book.getAvailableCopies() > book.getTotalCopies()) {
            throw new BadRequestException("Available copies cannot exceed total copies");
        }

        return BookMapper.toDto(bookRepository.save(book));
    }

    /**
     * Supprime un livre.
     *
     * @param id identifiant livre.
     */
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("Book not found");
        }
        bookRepository.deleteById(id);
    }

    /**
     * Met à jour le nombre de copies.
     *
     * @param id identifiant livre.
     * @param request valeurs/variations des copies.
     * @return livre mis à jour.
     */
    public BookDto updateCopies(Long id, UpdateCopiesRequest request) {
        Book book = getBook(id);

        if (request.getDeltaTotal() != null) {
            book.setTotalCopies(book.getTotalCopies() + request.getDeltaTotal());
        }
        if (request.getDeltaAvailable() != null) {
            book.setAvailableCopies(book.getAvailableCopies() + request.getDeltaAvailable());
        }
        if (request.getTotalCopies() != null) {
            book.setTotalCopies(request.getTotalCopies());
        }
        if (request.getAvailableCopies() != null) {
            book.setAvailableCopies(request.getAvailableCopies());
        }

        if (book.getTotalCopies() < 0 || book.getAvailableCopies() < 0) {
            throw new BadRequestException("Copies cannot be negative");
        }
        if (book.getAvailableCopies() > book.getTotalCopies()) {
            throw new BadRequestException("Available copies cannot exceed total copies");
        }

        return BookMapper.toDto(bookRepository.save(book));
    }

    private Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));
    }

    private Category resolveCategory(String name, String description) {
        Optional<Category> existing = categoryRepository.findByNameIgnoreCase(name);
        if (existing.isPresent()) {
            Category category = existing.get();
            if (description != null && !Objects.equals(category.getDescription(), description)) {
                category.setDescription(description);
                category = categoryRepository.save(category);
            }
            return category;
        }
        return categoryRepository.save(Category.builder()
                .name(name)
                .description(description)
                .build());
    }
}
