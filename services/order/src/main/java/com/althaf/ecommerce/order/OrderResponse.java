package com.althaf.ecommerce.order;

import com.althaf.ecommerce.orderline.OrderLine;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Integer id,
        String reference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        String customerId
) {
}
