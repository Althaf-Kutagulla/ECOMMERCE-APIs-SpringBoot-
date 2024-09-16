package com.althaf.ecommerce.order;

import com.althaf.ecommerce.customer.CustomerClient;
import com.althaf.ecommerce.exception.BusinessException;
import com.althaf.ecommerce.kafka.OrderConfirmation;
import com.althaf.ecommerce.kafka.OrderProducer;
import com.althaf.ecommerce.orderline.OrderLineRequest;
import com.althaf.ecommerce.orderline.OrderLineService;
import com.althaf.ecommerce.payment.PaymentClient;
import com.althaf.ecommerce.payment.PaymentRequest;
import com.althaf.ecommerce.product.ProductClient;
import com.althaf.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;
    public Integer createOrder(OrderRequest request) {
        var customer = customerClient.findCustomerById(request.customerId())
                .orElseThrow(()->new BusinessException("Cannot create order::Customer not found with Id::"+request.customerId()));

        //purchase the products --> product-ms
        var purchasedProducts = productClient.purchaseProducts(request.products());

        BigDecimal totalAmount = purchasedProducts.stream()
                .map(e-> e.price().multiply(BigDecimal.valueOf(e.quantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        //generate Order reference
        var successFullOrder  = new OrderRequest(
                request.id(),
                generateOrderReference(),
                totalAmount,
                request.paymentMethod(),
                request.customerId(),
                request.products()
        );

        //persist order
        var order =this.repository.save(mapper.toOrder(successFullOrder));

        //persist order lines
        for(PurchaseRequest purchaseRequest: request.products()){
           OrderLineRequest orderLineRequest = new OrderLineRequest(null, order.getId(), purchaseRequest.productId(), purchaseRequest.quantity());
            orderLineService.saveOrderLine(orderLineRequest);
        }

        //todo start payment process
        var paymentRequest = new PaymentRequest(
                totalAmount,
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

        //send order confirmation --> notification-ms (kafka)
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(request.reference(),
                        totalAmount,
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );
        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return repository
                .findAll()
                .stream()
                .map(mapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer orderId) {
        return repository
                .findById(orderId)
                .map(mapper::fromOrder)
                .orElseThrow(()-> new EntityNotFoundException("No Order found with ID::"+orderId));
    }

    public static String generateOrderReference() {
        return "ORDER-" + UUID.randomUUID().toString();
    }
}
