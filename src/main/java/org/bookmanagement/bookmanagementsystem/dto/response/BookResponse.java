package org.bookmanagement.bookmanagementsystem.dto.response;

public record BookResponse(
        Long id,
        String isbn,
        String title,
        String authorName,
        String genre,
        Integer publicationYear,
        Integer totalCopies,
        Integer availableCopies,
        Long publisherId,
        String publisherName
) {}
