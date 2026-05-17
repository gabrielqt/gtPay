package com.gabrielqt.gtpay.entity;


import com.gabrielqt.gtpay.entity.enums.CardBrand;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "payments_card")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@DiscriminatorValue("CARD")
@EqualsAndHashCode(callSuper = true) // ← inclui campos da superclasse
public class PaymentCard extends Payment {
    private Integer lastDigits;
    @Enumerated(EnumType.STRING)
    private CardBrand cardBrand;
    @NotBlank
    private String holderName;
}
