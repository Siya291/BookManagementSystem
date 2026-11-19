package org.bookmanagement.bookmanagementsystem.controller;

import jakarta.validation.Valid;
import org.bookmanagement.bookmanagementsystem.dto.request.TransactionRequest;
import org.bookmanagement.bookmanagementsystem.dto.response.TransactionResponse;
import org.bookmanagement.bookmanagementsystem.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/borrow")
    public ResponseEntity<TransactionResponse> borrowBook(@Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.borrowBook(request));
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<TransactionResponse> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.returnBook(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions(
            @RequestParam(required = false) Long borrowerId
    ) {
        if (borrowerId != null) {
            return ResponseEntity.ok(transactionService.getTransactionsByBorrower(borrowerId));
        }
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
}