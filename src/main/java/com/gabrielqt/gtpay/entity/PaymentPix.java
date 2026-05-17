package com.gabrielqt.gtpay.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "payments_pix")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@DiscriminatorValue("PIX")
@EqualsAndHashCode(callSuper = true) // ← inclui campos da superclasse
public class PaymentPix extends Payment {
    private String pixKey;
}
