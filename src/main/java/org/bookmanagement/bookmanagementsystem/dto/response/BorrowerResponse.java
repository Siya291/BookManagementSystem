package org.bookmanagement.bookmanagementsystem.dto.response;

import org.bookmanagement.bookmanagementsystem.entity.enums.AccountStatus;
import java.time.LocalDate;

public record BorrowerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String address,
        LocalDate registrationDate,
        AccountStatus accountStatus
) {}
