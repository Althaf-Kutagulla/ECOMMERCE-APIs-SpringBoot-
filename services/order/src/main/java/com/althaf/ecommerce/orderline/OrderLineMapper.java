package com.althaf.ecommerce.orderline;

import com.althaf.ecommerce.order.Order;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class OrderLineMapper {



    public OrderLine toOrderLine(OrderLineRequest orderLineRequest) {
        return OrderLine.builder()
                .id(orderLineRequest.id())
                .quantity(orderLineRequest.quantity())
                .productId(orderLineRequest.productId())
                .order(Order.builder().id(orderLineRequest.orderId()).build())
                .build();
    }

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        return new OrderLineResponse(orderLine.getId(), orderLine.getQuantity());
    }

}
