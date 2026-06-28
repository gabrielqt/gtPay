package com.gabrielqt.gtpay.service;

import com.gabrielqt.gtpay.entity.Charge;
import com.gabrielqt.gtpay.entity.WebhookEvent;
import com.gabrielqt.gtpay.entity.WebhookSubscription;
import com.gabrielqt.gtpay.entity.enums.Status;
import com.gabrielqt.gtpay.repository.WebhookEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebhookEventService {
    private final WebhookEventRepository webhookEventRepository;
    private final WebhookSubscriptionService webhookSubscriptionService;

    public List<WebhookEvent> findUndelivered() {
        return webhookEventRepository.findByDeliveredFalseAndAttemptsLessThan(10);
    }

    public WebhookEvent save(WebhookEvent webhookEvent) {
        return webhookEventRepository.save(webhookEvent);
    }

}
