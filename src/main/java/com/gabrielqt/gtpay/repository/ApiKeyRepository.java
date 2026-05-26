package com.gabrielqt.gtpay.repository;

import com.gabrielqt.gtpay.entity.ApiKey;
import com.gabrielqt.gtpay.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    Optional<Merchant> findMerchantByApiKey(String key);
}
