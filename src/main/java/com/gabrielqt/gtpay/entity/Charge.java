package com.gabrielqt.gtpay.entity;

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
    private Status status; // quero setar default para pending

    @NotNull
    @Min(0)
    private BigDecimal amount;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private Long externalId; // charge id on the merchant system

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
