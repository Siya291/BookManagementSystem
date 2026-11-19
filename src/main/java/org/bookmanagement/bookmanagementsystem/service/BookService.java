package org.bookmanagement.bookmanagementsystem.service;

import jakarta.persistence.EntityNotFoundException;
import org.bookmanagement.bookmanagementsystem.dto.request.BookRequest;
import org.bookmanagement.bookmanagementsystem.dto.response.BookResponse;
import org.bookmanagement.bookmanagementsystem.entity.Book;
import org.bookmanagement.bookmanagementsystem.entity.Publisher;
import org.bookmanagement.bookmanagementsystem.repository.BookRepository;
import org.bookmanagement.bookmanagementsystem.repository.PublisherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;

    public BookService(BookRepository bookRepository, PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
    }

    @Transactional
    public BookResponse createBook(BookRequest request) {
        if (bookRepository.existsByIsbn(request.isbn())) {
            throw new IllegalArgumentException("Book with ISBN " + request.isbn() + " already exists.");
        }

        Publisher publisher = publisherRepository.findById(request.publisherId())
                .orElseThrow(() -> new EntityNotFoundException("Publisher not found with ID: " + request.publisherId()));

        Book book = new Book();
        book.setIsbn(request.isbn());
        book.setTitle(request.title());
        book.setAuthorName(request.authorName());
        book.setGenre(request.genre());
        book.setPublicationYear(request.publicationYear());
        book.setTotalCopies(request.totalCopies());
        book.setAvailableCopies(request.totalCopies());
        book.setPublisher(publisher);

        return mapToResponse(bookRepository.save(book));
    }

    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + id));
        return mapToResponse(book);
    }

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<BookResponse> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + id));

        if (!book.getIsbn().equals(request.isbn()) && bookRepository.existsByIsbn(request.isbn())) {
            throw new IllegalArgumentException("Another book with ISBN " + request.isbn() + " already exists.");
        }

        Publisher publisher = publisherRepository.findById(request.publisherId())
                .orElseThrow(() -> new EntityNotFoundException("Publisher not found with ID: " + request.publisherId()));

        int borrowedCopies = book.getTotalCopies() - book.getAvailableCopies();
        if (request.totalCopies() < borrowedCopies) {
            throw new IllegalStateException("Total copies cannot be less than currently borrowed copies (" + borrowedCopies + ").");
        }

        book.setIsbn(request.isbn());
        book.setTitle(request.title());
        book.setAuthorName(request.authorName());
        book.setGenre(request.genre());
        book.setPublicationYear(request.publicationYear());
        book.setTotalCopies(request.totalCopies());
        book.setAvailableCopies(request.totalCopies() - borrowedCopies);
        book.setPublisher(publisher);

        return mapToResponse(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found with ID: " + id);
        }
        bookRepository.deleteById(id);
    }

    private BookResponse mapToResponse(Book book) {

        return new BookResponse(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthorName(),
                book.getGenre(),
                book.getPublicationYear(),
                book.getTotalCopies(),
                book.getAvailableCopies(),
                book.getPublisher().getId(),
                book.getPublisher().getFullName()
        );
    }
}
