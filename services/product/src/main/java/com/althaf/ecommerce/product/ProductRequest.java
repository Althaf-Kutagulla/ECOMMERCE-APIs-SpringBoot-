package com.althaf.ecommerce.product;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        Integer id,
        @NotNull(message = "Product name should be required.")
        String name,
        @NotNull(message = "Product description should be required.")
        String description,
        @Positive(message = "Available quantity should be positive.")
        double availableQuantity,
        @Positive(message = "Product price should be positive.")
        BigDecimal price,
        @NotNull(message = "Category Id should not be null.")
        Integer categoryId
) {
}
