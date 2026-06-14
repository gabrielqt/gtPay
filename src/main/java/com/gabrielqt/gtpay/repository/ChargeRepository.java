package com.gabrielqt.gtpay.repository;

import com.gabrielqt.gtpay.entity.Charge;
import com.gabrielqt.gtpay.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChargeRepository extends JpaRepository<Charge, Long> {
    Optional<Charge> findByExternalId(String id);
    boolean existsByExternalIdAndMerchant(String id, Merchant merchant);
}
