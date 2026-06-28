package com.gabrielqt.gtpay.repository;

import com.gabrielqt.gtpay.entity.Merchant;
import com.gabrielqt.gtpay.entity.WebhookSubscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WebhookSubscriptionRepository extends JpaRepository<WebhookSubscription, Long> {
    boolean existsByMerchantIdAndPath(Long merchantId, String path);
    Page<WebhookSubscription> findByMerchant(Pageable pageable, Merchant merchant);
    List<WebhookSubscription>findByMerchant(Merchant merchant);
}
