package com.althaf.ecommerce.payment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.aspectj.bridge.IMessage;
import org.springframework.validation.annotation.Validated;

@Validated
public record Customer(
    String id,
    @NotNull(message="Firstname is required")
    String firstName,
    @NotNull(message = "Lastname is required")
    String lastName,
    @NotNull(message = "Message is required")
    @Email(message="email should be in right format")
    String email
) {
}
