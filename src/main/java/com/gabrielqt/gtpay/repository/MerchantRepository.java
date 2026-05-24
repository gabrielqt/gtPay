package com.gabrielqt.gtpay.repository;

import com.gabrielqt.gtpay.entity.Merchant;
import com.gabrielqt.gtpay.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Optional<Merchant> findByUser(User user);
}
