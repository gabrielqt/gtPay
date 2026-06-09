package com.gabrielqt.gtpay.dto.response;

import com.gabrielqt.gtpay.entity.enums.EventType;

public record WebhookSubscriptionResponse(
        Long id,
        String path,
        EventType event,
        String secret,
        boolean isActive
) {
}
