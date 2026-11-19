package org.bookmanagement.bookmanagementsystem.controller;

import jakarta.validation.Valid;
import org.bookmanagement.bookmanagementsystem.dto.request.BorrowerRequest;
import org.bookmanagement.bookmanagementsystem.dto.response.BorrowerResponse;
import org.bookmanagement.bookmanagementsystem.entity.enums.AccountStatus;
import org.bookmanagement.bookmanagementsystem.service.BorrowerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {

    private final BorrowerService borrowerService;

    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @PostMapping
    public ResponseEntity<BorrowerResponse> createBorrower(@Valid @RequestBody BorrowerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(borrowerService.createBorrower(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowerResponse> getBorrowerById(@PathVariable Long id) {
        return ResponseEntity.ok(borrowerService.getBorrowerById(id));
    }

    @GetMapping
    public ResponseEntity<List<BorrowerResponse>> getAllBorrowers() {
        return ResponseEntity.ok(borrowerService.getAllBorrowers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BorrowerResponse> updateBorrower(
            @PathVariable Long id,
            @Valid @RequestBody BorrowerRequest request
    ) {
        return ResponseEntity.ok(borrowerService.updateBorrower(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BorrowerResponse> updateAccountStatus(
            @PathVariable Long id,
            @RequestParam AccountStatus status
    ) {
        return ResponseEntity.ok(borrowerService.updateAccountStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrower(@PathVariable Long id) {
        borrowerService.deleteBorrower(id);
        return ResponseEntity.noContent().build();
    }
}