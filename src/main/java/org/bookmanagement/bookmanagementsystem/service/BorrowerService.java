package org.bookmanagement.bookmanagementsystem.service;

import jakarta.persistence.EntityNotFoundException;
import org.bookmanagement.bookmanagementsystem.dto.request.BorrowerRequest;
import org.bookmanagement.bookmanagementsystem.dto.response.BorrowerResponse;
import org.bookmanagement.bookmanagementsystem.entity.Borrower;
import org.bookmanagement.bookmanagementsystem.entity.enums.AccountStatus;
import org.bookmanagement.bookmanagementsystem.repository.BorrowerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;

    public BorrowerService(BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    @Transactional
    public BorrowerResponse createBorrower(BorrowerRequest request) {
        if (borrowerRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already registered: " + request.email());
        }

        Borrower borrower = new Borrower();
        borrower.setFirstName(request.firstName());
        borrower.setLastName(request.lastName());
        borrower.setEmail(request.email());
        borrower.setPhone(request.phone());
        borrower.setAddress(request.address());

        return mapToResponse(borrowerRepository.save(borrower));
    }

    public BorrowerResponse getBorrowerById(Long id) {
        Borrower borrower = borrowerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Borrower not found with ID: " + id));
        return mapToResponse(borrower);
    }

    public List<BorrowerResponse> getAllBorrowers() {
        return borrowerRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public BorrowerResponse updateBorrower(Long id, BorrowerRequest request) {
        Borrower borrower = borrowerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Borrower not found with ID: " + id));

        if (!borrower.getEmail().equals(request.email()) && borrowerRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already in use: " + request.email());
        }

        borrower.setFirstName(request.firstName());
        borrower.setLastName(request.lastName());
        borrower.setEmail(request.email());
        borrower.setPhone(request.phone());
        borrower.setAddress(request.address());

        return mapToResponse(borrowerRepository.save(borrower));
    }

    @Transactional
    public BorrowerResponse updateAccountStatus(Long id, AccountStatus status) {
        Borrower borrower = borrowerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Borrower not found with ID: " + id));
        borrower.setAccountStatus(status);
        return mapToResponse(borrowerRepository.save(borrower));
    }

    @Transactional
    public void deleteBorrower(Long id) {
        if (!borrowerRepository.existsById(id)) {
            throw new EntityNotFoundException("Borrower not found with ID: " + id);
        }
        borrowerRepository.deleteById(id);
    }

    private BorrowerResponse mapToResponse(Borrower borrower) {

        return new BorrowerResponse(
                borrower.getId(),
                borrower.getFirstName(),
                borrower.getLastName(),
                borrower.getEmail(),
                borrower.getPhone(),
                borrower.getAddress(),
                borrower.getRegistrationDate(),
                borrower.getAccountStatus()
        );
    }
}
