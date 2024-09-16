package com.althaf.ecommerce.payment;

import com.althaf.ecommerce.customer.CustomerResponse;
import com.althaf.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
