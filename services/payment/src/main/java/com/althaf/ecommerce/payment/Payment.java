package com.althaf.ecommerce.payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private Integer orderId;
    @CreatedDate
    @JoinColumn(updatable = false,nullable = false)
    private LocalDateTime createdAt;
    @JoinColumn(insertable = false, nullable = true)
    private LocalDateTime lastModifiedAt;

}
