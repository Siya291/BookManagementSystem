package org.bookmanagement.bookmanagementsystem.service;

import jakarta.persistence.EntityNotFoundException;
import org.bookmanagement.bookmanagementsystem.dto.request.TransactionRequest;
import org.bookmanagement.bookmanagementsystem.dto.response.TransactionResponse;
import org.bookmanagement.bookmanagementsystem.entity.Book;
import org.bookmanagement.bookmanagementsystem.entity.Borrower;
import org.bookmanagement.bookmanagementsystem.entity.Transaction;
import org.bookmanagement.bookmanagementsystem.entity.enums.AccountStatus;
import org.bookmanagement.bookmanagementsystem.entity.enums.TransactionStatus;
import org.bookmanagement.bookmanagementsystem.repository.BookRepository;
import org.bookmanagement.bookmanagementsystem.repository.BorrowerRepository;
import org.bookmanagement.bookmanagementsystem.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TransactionService {

    private static final int MAX_BORROW_LIMIT = 5;
    private static final BigDecimal DAILY_FINE_RATE = BigDecimal.ONE; // $1.00 per overdue day

    private final TransactionRepository transactionRepository;
    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            BookRepository bookRepository,
            BorrowerRepository borrowerRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.bookRepository = bookRepository;
        this.borrowerRepository = borrowerRepository;
    }

    @Transactional
    public TransactionResponse borrowBook(TransactionRequest request) {
        Borrower borrower = borrowerRepository.findById(request.borrowerId())
                .orElseThrow(() -> new EntityNotFoundException("Borrower not found with ID: " + request.borrowerId()));

        if (borrower.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Borrower account is suspended and cannot borrow books.");
        }

        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + request.bookId()));

        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No available copies for book: " + book.getTitle());
        }

        if (transactionRepository.existsByBorrowerIdAndBookIdAndStatus(
                borrower.getId(), book.getId(), TransactionStatus.BORROWED)) {
            throw new IllegalStateException("Borrower already has an active loan for this book.");
        }

        long activeLoans = transactionRepository.countByBorrowerIdAndStatus(
                borrower.getId(), TransactionStatus.BORROWED);
        if (activeLoans >= MAX_BORROW_LIMIT) {
            throw new IllegalStateException("Borrower has reached the maximum limit of " + MAX_BORROW_LIMIT + " borrowed books.");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        Transaction transaction = new Transaction();
        transaction.setBook(book);
        transaction.setBorrower(borrower);
        transaction.setBorrowDate(LocalDate.now());
        transaction.setDueDate(request.dueDate());
        transaction.setStatus(TransactionStatus.BORROWED);

        return mapToResponse(transactionRepository.save(transaction));
    }

    @Transactional
    public TransactionResponse returnBook(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with ID: " + transactionId));

        if (transaction.getStatus() == TransactionStatus.RETURNED) {
            throw new IllegalStateException("This book has already been returned.");
        }

        LocalDate today = LocalDate.now();
        transaction.setReturnDate(today);
        transaction.setStatus(TransactionStatus.RETURNED);

        if (today.isAfter(transaction.getDueDate())) {
            long daysOverdue = ChronoUnit.DAYS.between(transaction.getDueDate(), today);
            transaction.setFineAmount(DAILY_FINE_RATE.multiply(BigDecimal.valueOf(daysOverdue)));
        }

        Book book = transaction.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return mapToResponse(transactionRepository.save(transaction));
    }

    public TransactionResponse getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with ID: " + id));
        return mapToResponse(transaction);
    }

    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<TransactionResponse> getTransactionsByBorrower(Long borrowerId) {
        return transactionRepository.findByBorrowerId(borrowerId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private TransactionResponse mapToResponse(Transaction transaction) {

        return new TransactionResponse(
                transaction.getId(),
                transaction.getBook().getId(),
                transaction.getBook().getTitle(),
                transaction.getBook().getIsbn(),
                transaction.getBorrower().getId(),
                transaction.getBorrower().getFirstName(),
                transaction.getBorrowDate(),
                transaction.getDueDate(),
                transaction.getReturnDate(),
                transaction.getFineAmount(),
                transaction.getStatus()
        );
    }
}
