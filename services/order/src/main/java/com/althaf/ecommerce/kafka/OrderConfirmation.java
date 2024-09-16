package com.althaf.ecommerce.kafka;

import com.althaf.ecommerce.customer.CustomerResponse;
import com.althaf.ecommerce.order.PaymentMethod;
import com.althaf.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
