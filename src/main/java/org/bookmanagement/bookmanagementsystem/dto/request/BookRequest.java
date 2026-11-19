package org.bookmanagement.bookmanagementsystem.dto.request;

import jakarta.validation.constraints.*;

public record BookRequest(
        @NotBlank(message = "ISBN is required")
        @Size(min = 10, max = 17, message = "ISBN must be between 10 and 17 characters")
        String isbn,

        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,

        @NotBlank(message = "Author name is required")
        @Size(max = 150, message = "Author name must not exceed 150 characters")
        String authorName,

        @NotBlank(message = "Genre is required")
        @Size(max = 100, message = "Genre must not exceed 100 characters")
        String genre,

        @NotNull(message = "Publication year is required")
        @Min(value = 1000, message = "Invalid publication year")
        Integer publicationYear,

        @NotNull(message = "Total copies is required")
        @Min(value = 1, message = "Total copies must be at least 1")
        Integer totalCopies,

        @NotNull(message = "Publisher ID is required")
        Long publisherId
) {}