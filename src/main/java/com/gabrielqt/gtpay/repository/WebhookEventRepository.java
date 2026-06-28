package com.gabrielqt.gtpay.repository;

import com.gabrielqt.gtpay.entity.Charge;
import com.gabrielqt.gtpay.entity.WebhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebhookEventRepository extends JpaRepository<WebhookEvent, Long> {
    List<WebhookEvent> findEventByChargeId(Long chargeId);
    List<WebhookEvent> findByDeliveredFalseAndAttemptsLessThan(int maxAttempts);
}
