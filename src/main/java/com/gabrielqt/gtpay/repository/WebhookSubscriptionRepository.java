package com.gabrielqt.gtpay.repository;

import com.gabrielqt.gtpay.entity.Merchant;
import com.gabrielqt.gtpay.entity.WebhookSubscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebhookSubscriptionRepository extends JpaRepository<WebhookSubscription, Long> {
    boolean existsByMerchantIdAndPath(Long merchantId, String path);
    Page<WebhookSubscription> findByMerchant(Pageable pageable, Merchant merchant);
}
