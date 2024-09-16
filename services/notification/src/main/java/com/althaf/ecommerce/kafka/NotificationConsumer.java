package com.althaf.ecommerce.kafka;

import com.althaf.ecommerce.kafka.order.OrderConfirmation;
import com.althaf.ecommerce.kafka.payment.PaymentConfirmation;
import com.althaf.ecommerce.notification.Notification;
import com.althaf.ecommerce.notification.NotificationRepository;
import com.althaf.ecommerce.notification.NotificationType;
import com.althaf.ecommerce.email.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @KafkaListener(topics = "payment-topic", groupId = "paymentGroup")
    public void consumePaymentSuccessNotification(PaymentConfirmation paymentConfirmation) throws MessagingException {
        log.info(String.format("Consuming message from payment-topic Topic:: %s",paymentConfirmation));
        notificationRepository.save(
                Notification.builder()
                        .type(NotificationType.PAYMENT_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .paymentConfirmation(paymentConfirmation)
                .build()
        );
        // todo email send
        emailService.sentPaymentSuccessEmail(paymentConfirmation.customerEmail(),
                paymentConfirmation.customerFirstname()+" "+paymentConfirmation.customerLastname(),
                paymentConfirmation.amount(),
                paymentConfirmation.orderReference());
    }

    @KafkaListener(topics = "order-topic",groupId = "orderGroup")
    public void consumePaymentSuccessNotification(OrderConfirmation orderConfirmation) throws MessagingException {
        log.info(String.format("Consuming message from order-topic Topic:: %s",orderConfirmation));
        notificationRepository.save(
                Notification.builder()
                        .type(NotificationType.ORDER_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .orderConfirmation(orderConfirmation)
                        .build()
        );
        // todo email send
        emailService.sentOrderConfirmationEmail(orderConfirmation.customer().email(),
                orderConfirmation.customer().firstName()+" "+orderConfirmation.customer().lastName(),
                orderConfirmation.totalAmount(),
                orderConfirmation.orderReference(),
                orderConfirmation.products());
    }
}
