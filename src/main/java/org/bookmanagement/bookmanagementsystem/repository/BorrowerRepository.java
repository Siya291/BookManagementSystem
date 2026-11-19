package org.bookmanagement.bookmanagementsystem.repository;

import org.bookmanagement.bookmanagementsystem.entity.Borrower;
import org.bookmanagement.bookmanagementsystem.entity.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {

    Optional<Borrower> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Borrower> findByAccountStatus(AccountStatus accountStatus);

    List<Borrower> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
}
