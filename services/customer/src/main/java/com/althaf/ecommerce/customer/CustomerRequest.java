package com.althaf.ecommerce.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
        String id,
        @NotNull(message = "First Name is required")
        String firstName,
        @NotNull(message = "Last Name is required")
        String lastName,
        @NotNull(message = "Email is required")
        @Email(message = "Email should be in correct format")
        String email,


        Address address
) {
}
