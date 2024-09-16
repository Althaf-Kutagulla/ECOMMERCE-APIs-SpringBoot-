package com.althaf.ecommerce.payment;

import com.althaf.ecommerce.notification.NotificationProducer;
import com.althaf.ecommerce.notification.PaymentNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final NotificationProducer notificationProducer;

    public Integer createPayment(PaymentRequest request){
        notificationProducer.sendNotification(new PaymentNotificationRequest(
                request.orderReference(),
                request.amount(),
                request.paymentMethod(),
                request.customer().firstName(),
                request.customer().lastName(),
                request.customer().email()
        ));
        return repository.save(mapper.toPayment(request)).getId();
    }

}
