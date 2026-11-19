package org.bookmanagement.bookmanagementsystem.repository;

import org.bookmanagement.bookmanagementsystem.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorNameContainingIgnoreCase(String authorName);

    List<Book> findByGenreIgnoreCase(String genre);

    List<Book> findByPublisherId(Long publisherId);

    List<Book> findByAvailableCopiesGreaterThan(int minCopies);
}
