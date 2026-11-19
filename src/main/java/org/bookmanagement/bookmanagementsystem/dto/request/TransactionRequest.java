package org.bookmanagement.bookmanagementsystem.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record TransactionRequest(
        @NotNull(message = "Book ID is required")
        Long bookId,

        @NotNull(message = "Borrower ID is required")
        Long borrowerId,

        @NotNull(message = "Due date is required")
        @Future(message = "Due date must be in the future")
        LocalDate dueDate
) {}
