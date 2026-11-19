package org.bookmanagement.bookmanagementsystem.dto.response;

public record PublisherResponse(
        Long id,
        String fullName,
        String email,
        String phone
) {}
