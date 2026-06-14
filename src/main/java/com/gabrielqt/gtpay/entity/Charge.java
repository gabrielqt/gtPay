package com.gabrielqt.gtpay.entity;

import com.gabrielqt.gtpay.entity.enums.PaymentType;
import com.gabrielqt.gtpay.entity.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "charge")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Charge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @NotNull
    @Min(value = 0, message = "Amount must be greater than or equal to zero")
    private BigDecimal amount;

    @Column(name = "br_code", columnDefinition = "TEXT")
    private String brCode;   // gerado se paymentType = PIX

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private String externalId;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
