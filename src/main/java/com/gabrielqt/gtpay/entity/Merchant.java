package com.gabrielqt.gtpay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "merchant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "base_url", unique = true, nullable = false)  //ex: https://api.com
    private String baseUrl;

    @OneToMany(mappedBy = "merchant")
    private List<MerchantApiKey> apiKeys;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


}
