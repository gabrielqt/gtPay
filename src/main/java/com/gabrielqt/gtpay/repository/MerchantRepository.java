package com.gabrielqt.gtpay.repository;

import com.gabrielqt.gtpay.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
}
