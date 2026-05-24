package com.gabrielqt.gtpay.service;


import com.gabrielqt.gtpay.dto.request.WebhookSubscriptionRequest;
import com.gabrielqt.gtpay.dto.response.WebhookSubscriptionResponse;
import com.gabrielqt.gtpay.entity.Merchant;
import com.gabrielqt.gtpay.entity.User;
import com.gabrielqt.gtpay.entity.WebhookSubscription;
import com.gabrielqt.gtpay.mapper.WebhookSubscriptionMapper;
import com.gabrielqt.gtpay.repository.WebhookSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.gabrielqt.gtpay.security.SecretService;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class WebhookSubscriptionService {
    private final MerchantService merchantService;
    private final SecretService secretService;
    private final WebhookSubscriptionMapper webhookSubscriptionMapper;
    private final WebhookSubscriptionRepository webhookSubscriptionRepository;

    public WebhookSubscriptionResponse createWebhookSubscription(WebhookSubscriptionRequest request, User user) {

        Merchant merchant = merchantService.findByUser(user);
        String secret = secretService.generateSecret();
        String secretEncrypted = secretService.encryptSecret(secret);
        WebhookSubscription webhookSubscription = webhookSubscriptionMapper.toEntity(request, merchant, secretEncrypted);
        webhookSubscriptionRepository.save(webhookSubscription);

        return webhookSubscriptionMapper.toResponse(webhookSubscription, secret);
    }


}

