package com.gabrielqt.gtpay.mapper;

import com.gabrielqt.gtpay.dto.request.WebhookSubscriptionRequest;
import com.gabrielqt.gtpay.dto.response.WebhookSubscriptionResponse;
import com.gabrielqt.gtpay.entity.Merchant;
import com.gabrielqt.gtpay.entity.WebhookSubscription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebhookSubscriptionMapper {
    public WebhookSubscription toEntity(WebhookSubscriptionRequest request,
                                        Merchant merchant,
                                        String secret) {
        return WebhookSubscription.builder()
                .merchant(merchant)
                .path(request.path())
                .isActive(true)
                .secretEncrypted(secret)
                .event(request.event())
                .build();
    }

    public WebhookSubscriptionResponse toResponse(WebhookSubscription webhookSubscription,
                                                  String secret) {
        return new WebhookSubscriptionResponse(
                webhookSubscription.getMerchant().getId(),
                webhookSubscription.getPath(),
                webhookSubscription.getEvent(),
                secret,
                webhookSubscription.isActive()
        );
    }

    public WebhookSubscriptionResponse toResponse(WebhookSubscription webhookSubscription
                                                 ) {
        return new WebhookSubscriptionResponse(
                webhookSubscription.getMerchant().getId(),
                webhookSubscription.getPath(),
                webhookSubscription.getEvent(),
                webhookSubscription.getSecretEncrypted(),
                webhookSubscription.isActive()
        );
    }
}
