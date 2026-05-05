package com.gabrielqt.gtpay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "merchant_apikey")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchantApiKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @Column(name = "key_hash", unique = true, nullable = false)
    private String keyHash;
}
