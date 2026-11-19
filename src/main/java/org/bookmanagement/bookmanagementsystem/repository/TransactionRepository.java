package org.bookmanagement.bookmanagementsystem.repository;

import org.bookmanagement.bookmanagementsystem.entity.Transaction;
import org.bookmanagement.bookmanagementsystem.entity.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByBorrowerId(Long borrowerId);

    List<Transaction> findByBookId(Long bookId);

    List<Transaction> findByStatus(TransactionStatus status);

    long countByBorrowerIdAndStatus(Long borrowerId, TransactionStatus status);

    boolean existsByBorrowerIdAndBookIdAndStatus(Long borrowerId, Long bookId, TransactionStatus status);

    List<Transaction> findByStatusAndDueDateBefore(TransactionStatus status, LocalDate date);
}
