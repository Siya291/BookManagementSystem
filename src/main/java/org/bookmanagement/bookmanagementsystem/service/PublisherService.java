package org.bookmanagement.bookmanagementsystem.service;

import jakarta.persistence.EntityNotFoundException;
import org.bookmanagement.bookmanagementsystem.dto.request.PublisherRequest;
import org.bookmanagement.bookmanagementsystem.dto.response.PublisherResponse;
import org.bookmanagement.bookmanagementsystem.entity.Publisher;
import org.bookmanagement.bookmanagementsystem.repository.PublisherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Transactional
    public PublisherResponse createPublisher(PublisherRequest request) {
        if (publisherRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Publisher with email " + request.email() + " already exists.");
        }

        Publisher publisher = new Publisher();
        publisher.setFullName(request.fullName());
        publisher.setEmail(request.email());
        publisher.setPhone(request.phone());

        return mapToResponse(publisherRepository.save(publisher));
    }

    public PublisherResponse getPublisherById(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publisher not found with ID: " + id));
        return mapToResponse(publisher);
    }

    public List<PublisherResponse> getAllPublishers() {
        return publisherRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public PublisherResponse updatePublisher(Long id, PublisherRequest request) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publisher not found with ID: " + id));

        if (!publisher.getEmail().equals(request.email()) && publisherRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already in use: " + request.email());
        }

        publisher.setFullName(request.fullName());
        publisher.setEmail(request.email());
        publisher.setPhone(request.phone());

        return mapToResponse(publisherRepository.save(publisher));
    }

    @Transactional
    public void deletePublisher(Long id) {
        if (!publisherRepository.existsById(id)) {
            throw new EntityNotFoundException("Publisher not found with ID: " + id);
        }
        publisherRepository.deleteById(id);
    }

    private PublisherResponse mapToResponse(Publisher publisher) {

        return new PublisherResponse(
                publisher.getId(),
                publisher.getFullName(),
                publisher.getEmail(),
                publisher.getPhone()
        );
    }
}
