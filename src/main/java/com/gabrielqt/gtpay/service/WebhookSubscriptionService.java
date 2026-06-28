package com.gabrielqt.gtpay.service;


import com.gabrielqt.gtpay.dto.request.WebhookSubscriptionRequest;
import com.gabrielqt.gtpay.dto.response.WebhookSubscriptionResponse;
import com.gabrielqt.gtpay.entity.Merchant;
import com.gabrielqt.gtpay.entity.User;
import com.gabrielqt.gtpay.entity.WebhookSubscription;
import com.gabrielqt.gtpay.entity.enums.EventType;
import com.gabrielqt.gtpay.entity.enums.Status;
import com.gabrielqt.gtpay.exception.BusinessException;
import com.gabrielqt.gtpay.exception.MerchantAndPathAlreadyExists;
import com.gabrielqt.gtpay.exception.MerchantWithoutBaseUrlException;
import com.gabrielqt.gtpay.exception.ObjectNotFoundException;
import com.gabrielqt.gtpay.mapper.WebhookSubscriptionMapper;
import com.gabrielqt.gtpay.repository.WebhookSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.gabrielqt.gtpay.security.SecretService;

import java.util.List;

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


    public void deleteWebhookSubscription(Long id) {
        WebhookSubscription webhookSubscription = findById(id);
        webhookSubscriptionRepository.delete(webhookSubscription);
    }

    public Page<WebhookSubscriptionResponse> findAll(Pageable pageable, User user) {
        return webhookSubscriptionRepository.findByMerchant(pageable, merchantService.findByUser(user))
                .map(webhookSubscriptionMapper::toResponse);
    }

    private boolean existsByMerchantIdAndPath(Long merchantId, String path) {
        return webhookSubscriptionRepository.existsByMerchantIdAndPath(merchantId, path);
    }

    public WebhookSubscription findById(Long id) {
        return webhookSubscriptionRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Webhook subscription with id " + id + " not found"));
    }

    public List<WebhookSubscription> findCompatibleWebhookSubscriptionByStatus(Merchant merchant, Status status) {
        List<WebhookSubscription> subscriptions =  webhookSubscriptionRepository.findByMerchant(merchant);

        EventType event = statusToEventType(status);

        return
                subscriptions.stream().filter(
                        wh -> wh.getEvent() == event || wh.getEvent() == EventType.ALL
                        ).toList();
    }

    protected EventType statusToEventType(Status status) {
        return switch(status){
            case FAILED -> EventType.CHARGE_FAILED;
            case PAID -> EventType.CHARGE_PAID;
            case EXPIRED -> EventType.CHARGE_EXPIRED;
            default -> null;
        };
    }

}

