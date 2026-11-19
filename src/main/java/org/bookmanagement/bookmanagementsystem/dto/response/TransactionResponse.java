package org.bookmanagement.bookmanagementsystem.dto.response;

import org.bookmanagement.bookmanagementsystem.entity.enums.TransactionStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
        Long id,
        Long bookId,
        String bookTitle,
        String bookIsbn,
        Long borrowerId,
        String borrowerFullName,
        LocalDate borrowDate,
        LocalDate dueDate,
        LocalDate returnDate,
        BigDecimal fineAmount,
        TransactionStatus status
) {}
