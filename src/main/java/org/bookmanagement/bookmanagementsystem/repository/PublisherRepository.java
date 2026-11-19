package org.bookmanagement.bookmanagementsystem.repository;

import org.bookmanagement.bookmanagementsystem.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Publisher> findByFullNameContainingIgnoreCase(String fullName);
}
