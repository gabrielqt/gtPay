package com.gabrielqt.gtpay.service;


import com.gabrielqt.gtpay.dto.request.WebhookSubscriptionRequest;
import com.gabrielqt.gtpay.dto.response.WebhookSubscriptionResponse;
import com.gabrielqt.gtpay.entity.Merchant;
import com.gabrielqt.gtpay.entity.User;
import com.gabrielqt.gtpay.entity.WebhookSubscription;
import com.gabrielqt.gtpay.exception.MerchantAndPathAlreadyExists;
import com.gabrielqt.gtpay.exception.MerchantWithoutBaseUrlException;
import com.gabrielqt.gtpay.mapper.WebhookSubscriptionMapper;
import com.gabrielqt.gtpay.repository.WebhookSubscriptionRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.gabrielqt.gtpay.security.SecretService;

@Service
@RequiredArgsConstructor
public class WebhookSubscriptionService {
    private final MerchantService merchantService;
    private final SecretService secretService;
    private final WebhookSubscriptionMapper webhookSubscriptionMapper;
    private final WebhookSubscriptionRepository webhookSubscriptionRepository;

    public WebhookSubscriptionResponse createWebhookSubscription(WebhookSubscriptionRequest request, User user) {

        Merchant merchant = merchantService.findByUser(user);
        if (existsByMerchantIdAndPath(merchant.getId(), request.path())) throw new MerchantAndPathAlreadyExists(request.path(), merchant.getId());
        if (merchant.getBaseUrl() == null || merchant.getBaseUrl().isBlank()) throw new MerchantWithoutBaseUrlException();
        String decryptSecret = secretService.generateSecret();
        String secretEncrypted = secretService.encryptSecret(decryptSecret);
        WebhookSubscription webhookSubscription = webhookSubscriptionMapper.toEntity(request, merchant, secretEncrypted);
        webhookSubscriptionRepository.save(webhookSubscription);

        return webhookSubscriptionMapper.toResponse(webhookSubscription, decryptSecret);
    }

    private boolean existsByMerchantIdAndPath(Long merchantId, String path) {
        return webhookSubscriptionRepository.existsByMerchantIdAndPath(merchantId, path);
    }

    public Page<WebhookSubscriptionResponse> findAll(Pageable pageable, Merchant merchant) {
        return webhookSubscriptionRepository.findByMerchant(pageable, merchant)
                .map(webhookSubscriptionMapper::toResponse);
    }


}

