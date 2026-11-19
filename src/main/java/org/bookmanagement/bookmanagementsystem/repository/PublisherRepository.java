package org.bookmanagement.bookmanagementsystem.repository;

import org.bookmanagement.bookmanagementsystem.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
}
