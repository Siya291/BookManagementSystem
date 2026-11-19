package org.bookmanagement.bookmanagementsystem.dto.request;

import jakarta.validation.constraints.*;

public record PublisherRequest(
        @NotBlank(message = "Full name is required")
        @Size(max = 150, message = "Full name must not exceed 150 characters")
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Size(max = 150, message = "Email must not exceed 150 characters")
        String email,

        @NotBlank(message = "Phone number is required")
        @Size(min = 7, max = 20, message = "Phone number must be between 7 and 20 characters")
        String phone
) {}