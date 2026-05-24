package com.gabrielqt.gtpay.repository;

import com.gabrielqt.gtpay.entity.WebhookSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebhookSubscriptionRepository extends JpaRepository<WebhookSubscription, Long> {
    boolean existsByMerchantIdAndPath(Long merchantId, String path);
}
