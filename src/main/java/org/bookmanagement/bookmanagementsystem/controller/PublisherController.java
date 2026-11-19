package org.bookmanagement.bookmanagementsystem.controller;

import jakarta.validation.Valid;
import org.bookmanagement.bookmanagementsystem.dto.request.PublisherRequest;
import org.bookmanagement.bookmanagementsystem.dto.response.PublisherResponse;
import org.bookmanagement.bookmanagementsystem.service.PublisherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping
    public ResponseEntity<PublisherResponse> createPublisher(@Valid @RequestBody PublisherRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publisherService.createPublisher(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponse> getPublisherById(@PathVariable Long id) {
        return ResponseEntity.ok(publisherService.getPublisherById(id));
    }

    @GetMapping
    public ResponseEntity<List<PublisherResponse>> getAllPublishers() {
        return ResponseEntity.ok(publisherService.getAllPublishers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherResponse> updatePublisher(
            @PathVariable Long id,
            @Valid @RequestBody PublisherRequest request
    ) {
        return ResponseEntity.ok(publisherService.updatePublisher(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }
}