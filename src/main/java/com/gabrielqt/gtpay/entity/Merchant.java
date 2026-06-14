package com.gabrielqt.gtpay.entity;

import com.gabrielqt.gtpay.validators.interfaces.BaseUrl;
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

    @BaseUrl
    @Column(name = "base_url", unique = true)  //ex: https://api.com.br
    private String baseUrl;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String pixKey;

    private String cep;

    private String city;

    @Column(name = "store_name")
    private String storeName;
}
