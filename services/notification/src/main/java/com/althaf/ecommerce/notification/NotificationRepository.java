package com.althaf.ecommerce.notification;

import com.althaf.ecommerce.kafka.payment.PaymentConfirmation;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification,String> {
}
